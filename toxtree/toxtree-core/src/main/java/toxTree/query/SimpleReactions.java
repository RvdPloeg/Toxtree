/*
Copyright Ideaconsult Ltd. (C) 2005-2007 

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */
package toxTree.query;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.isomorphism.matchers.IQueryAtom;
import org.openscience.cdk.isomorphism.matchers.InverseSymbolSetQueryAtom;
import org.openscience.cdk.isomorphism.matchers.OrderQueryBond;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.isomorphism.matchers.SymbolQueryAtom;
import org.openscience.cdk.isomorphism.matchers.smarts.AromaticQueryBond;
import org.openscience.cdk.isomorphism.mcss.RMap;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import toxTree.exceptions.ReactionException;

/**
 * Handles hydrolysis and couple of metabolic reactions necessary to implement
 * Cramer rules Intended to be a general class to perform reactions on arbitrary
 * molecules ... Preconfigured reactions are stored as CML files in config
 * directory
 * 
 * @author Nina Jeliazkova <b>Modified</b> 2005-8-18
 */
public class SimpleReactions {
	protected static SmilesGenerator gen = SmilesGenerator.generic();

	protected static Logger logger = Logger.getLogger(SimpleReactions.class
			.getName());

	protected IReaction reaction = null;

	protected static IReaction[] hydrolysis = { null, null, null, null, null,
			null };

	protected static IReaction[] metabolic = { null, null, null, null };

	protected static IReaction[] hydrolysisPO4 = {// NEW
	null, null, null };

	protected static String[] hydrolize = {
			"toxTree/config/reactions/reaction-hydrolize-C(dO)O.cml",
			"toxTree/config/reactions/reaction-hydrolize-C(dO)S.cml",
			"toxTree/config/reactions/reaction-hydrolize-C(dS)O.cml",
			"toxTree/config/reactions/reaction-hydrolize-C(dS)S.cml",
			"toxTree/config/reactions/reaction-hydrolize-P(dO)(O)(O)O.cml",
			"toxTree/config/reactions/reaction-hydrolize-S(dO)(dO)O.cml" };

	protected static String[] metabolize = {
			"toxTree/config/reactions/reaction-metabolize-NdN.cml",
			"toxTree/config/reactions/reaction-metabolize-Npd.cml",
			"toxTree/config/reactions/reaction-metabolize-R-N.cml",
			"toxTree/config/reactions/reaction-metabolize-CdC.cml" };

	protected static String[] hydrolizePO4 = { // NEW
	"toxTree/config/reactions/reaction-hydrolize-P(dO)(O)(O)OX.cml",
			"toxTree/config/reactions/reaction-hydrolize-C(dO)O.cml",
			"toxTree/config/reactions/reaction-hydrolize-C(dO)S.cml" };

	/**
	 * 
	 */
	public SimpleReactions() {
		super();
	}

	protected IReaction loadPreconfiguredReaction(String filename)
			throws CDKException {
		logger.finer("Reading reaction from file\t" + filename);
		IReaction newReaction = null;

		InputStream stream = SimpleReactions.class.getClassLoader()
				.getResourceAsStream(filename);
		if (stream == null)
			throw new CDKException("Can't find " + filename);
		CMLReader reader = new CMLReader(stream);
		IChemFile chemFile = (IChemFile) reader
				.read((IChemObject) new org.openscience.cdk.ChemFile());
		for (int i = 0; i < chemFile.getChemSequenceCount(); i++) {
			IChemSequence seq = chemFile.getChemSequence(i);
			for (int j = 0; j < seq.getChemModelCount(); j++) {
				IChemModel model = seq.getChemModel(j);
				IReactionSet reactions = model.getReactionSet();
				for (int k = 0; k < reactions.getReactionCount(); k++) {
					newReaction = reactions.getReaction(k);
					if (newReaction != null)
						break;
				}
			}
		}
		try {
			reader.close();
			stream.close();
		} catch (IOException x) {
			logger.log(Level.SEVERE, "Error when reading reaction" + filename,
					x);
			return null;
		}
		reader = null;
		stream = null;
		return newReaction;
	}

	protected IReaction loadHydrolysisReaction(int index) throws CDKException {
		return loadPreconfiguredReaction(hydrolize[index]);
	}

	protected IReaction loadMetabolicReaction(int index) throws CDKException {
		return loadPreconfiguredReaction(metabolize[index]);
	}

	public IReaction getHydrolysisReaction(int index) throws CDKException {
		if (hydrolysis[index] == null)
			hydrolysis[index] = loadPreconfiguredReaction(hydrolize[index]);
		return hydrolysis[index];
	}

	public IReaction getMetabolicReaction(int index) throws CDKException {
		if (metabolic[index] == null)
			metabolic[index] = loadPreconfiguredReaction(metabolize[index]);
		return metabolic[index];
	}

	public IReaction getHydrolysisPO4Reaction(// NEW
			int index) throws CDKException {
		if (hydrolysisPO4[index] == null)
			hydrolysisPO4[index] = loadPreconfiguredReaction(hydrolizePO4[index]);
		return hydrolysisPO4[index];
	}

	public static int getHydrolysisReactionCount() {
		return hydrolize.length;
	}

	public static int getMetabolicReactionCount() {
		return metabolize.length;
	}

	public static int getHydrolysisPO4ReactionCount() {// NEW
		return hydrolizePO4.length;
	}

	public static IAtomContainerSet process(IAtomContainer mol,
			IReaction reaction) throws ReactionException {
		return process(mol, reaction, mol.getBuilder());
	}

	public static IAtomContainerSet process(IAtomContainer mol,
			IReaction reaction, IChemObjectBuilder builder)
			throws ReactionException {
		logger.fine("Process reaction\t" + reaction.getID());
		logger.finer("Molecule\t" + mol.getID());
		IAtomContainer reactant = reaction.getReactants().getAtomContainer(0);
		QueryAtomContainer q = createQueryContainer(reactant);
		q.setID(reaction.getID());

		// List list = UniversalIsomorphismTester.getSubgraphMaps(mol,q);
		MolAnalyser.clearVisitedFlags(mol);
		List list = FunctionalGroups.getUniqueBondMap(mol, q, false);
		// FunctionalGroups.markMaps(mol,q,list);
		if ((list == null) || (list.size() == 0)) {
			logger.finer("Can't perform this reaction, no relevant groups found\t"
					+ q.getID());
			return null;
		}
		logger.finer("Relevant groups found\t" + q.getID());
		if (reaction.getID().startsWith("reaction-hydrolize")) {
			// search for single bond between two non R atoms and break it
			return hydrolize(mol, q, list, builder);
		} else if (reaction.getID().startsWith("reaction-metabolize")) {
			if (reaction.getID().equals("reaction-metabolize-R-N"))
				return metabolize1(mol, q, list, builder);
			else
				return metabolize(mol, q, list, builder);

		} else
			throw new ReactionException(reaction.getID() + " not implemented!");
		// FunctionalGroups.markMaps(mol,q,list);

	}

	protected static IAtomContainerSet hydrolize(IAtomContainer a,
			QueryAtomContainer q, List list, IChemObjectBuilder builder)
			throws ReactionException {
		logger.fine("hydrolize\t" + q.getID());
		logger.finer("Molecule\t" + a.getID());
		IAtomContainer mol = null;
		try {
			mol = (IAtomContainer) a.clone();
		} catch (CloneNotSupportedException x) {
			throw new ReactionException(x);
		}
		int bondToSplitMap = -1;

		for (int i = 0; i < q.getBondCount(); i++) {
			IBond b = q.getBond(i);
			if ((b.getOrder() == CDKConstants.BONDORDER_SINGLE)
					&& (b.getAtom(0) instanceof SymbolQueryAtom)
					&& (b.getAtom(1) instanceof SymbolQueryAtom)) {
				bondToSplitMap = i;
				break;
			}
		}

		if (bondToSplitMap == -1)
			throw new ReactionException(
					"Hydrolize: Can't find the bond to break in the reaction pattern!");
		ArrayList bondsToSplitMol = new ArrayList(); // could be several
		// bonds to split

		for (int i = 0; i < list.size(); i++) {
			List thisMap = (List) list.get(i);
			for (int j = 0; j < thisMap.size(); j++) {
				RMap rmap = (RMap) thisMap.get(j);
				if (rmap.getId2() == bondToSplitMap) {
					bondsToSplitMol.add(mol.getBond(rmap.getId1()));

					// this is the bond to be broken
					break;
				}
			}
			// if (bondToSplitMol!=null) break;
		}
		if (bondsToSplitMol.size() == 0)
			new ReactionException(
					"Hydrolize: Can't find the bond to break in the reactant!");
		IBond bondToSplitMol = null;
		for (int b = 0; b < bondsToSplitMol.size(); b++) {
			bondToSplitMol = (IBond) bondsToSplitMol.get(b);
			// IAtom[] atoms = bondToSplitMol.getAtoms();
			int ohAtom = -1;
			int hAtom = -1;
			for (int i = 0; i < bondToSplitMol.getAtomCount(); i++) {
				IAtom atom = bondToSplitMol.getAtom(i);
				/*
				 * will search for a double bond, then this will be the atom to
				 * link to OH group. The other atom will be linked to an
				 * additional H
				 */
				List cb = mol.getConnectedBondsList(atom);
				for (int j = 0; j < cb.size(); j++)
					if (((IBond) cb.get(j)).getOrder() == CDKConstants.BONDORDER_DOUBLE) {
						ohAtom = i;
						hAtom = (i + 1) % 2;
						break;
					}
			}
			if ((ohAtom == -1) || (hAtom == -1))
				new ReactionException(
						"Hydrolize: Can't find specified atoms in the reactant!");

			// break the bond
			mol.removeElectronContainer(bondToSplitMol);

			// add H and link it to hAtom
			IAtom h = builder.newInstance(IAtom.class, "H");
			mol.addAtom(h);
			mol.addBond(builder.newInstance(IBond.class,
					bondToSplitMol.getAtom(hAtom), h,
					CDKConstants.BONDORDER_SINGLE));

			// add H-O-ohAtom
			h = builder.newInstance(IAtom.class, "H");
			mol.addAtom(h);
			IAtom o = builder.newInstance(IAtom.class, "O");
			mol.addAtom(o);
			mol.addBond(builder.newInstance(IBond.class, o, h,
					CDKConstants.BONDORDER_SINGLE));
			mol.addBond(builder.newInstance(IBond.class,
					bondToSplitMol.getAtom(ohAtom), o,
					CDKConstants.BONDORDER_SINGLE));
		}
		// finally separate the resulting molecules
		try {
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
			CDKHydrogenAdder.getInstance(mol.getBuilder()).addImplicitHydrogens(mol);
		} catch (Exception x) {
		}		
		IAtomContainerSet result = ConnectivityChecker
				.partitionIntoMolecules(mol);
		SimpleReactions.setResidualID(mol, result);
		return result;

	}

	private static void splitMetabolize1(IAtomContainer mol, IBond b,
			int bondToSplitMol, IChemObjectBuilder builder) {
		// IAtom[] atoms = b.getAtoms();

		// break the bond
		logger.finer("Break bond " + b.getAtom(0).getSymbol() + " "
				+ b.getAtom(1).getSymbol());
		mol.removeElectronContainer(bondToSplitMol);
		for (int i = 0; i < 2; i++) {
			// add H and link it to each atom f the broken bond
			IAtom h = builder.newInstance(IAtom.class, "H");
			mol.addAtom(h);
			mol.addBond(builder.newInstance(IBond.class, b.getAtom(i), h,
					CDKConstants.BONDORDER_SINGLE));
		}
	}

	private static IAtomContainerSet metabolize1(IAtomContainer a,
			QueryAtomContainer q, List list, IChemObjectBuilder builder)
			throws ReactionException {
		logger.finer("metabolize1\t" + q.getID());
		logger.fine("Molecule\t" + a.getID());
		IAtomContainer mol = null;
		try {
			mol = (IAtomContainer) a.clone();
		} catch (CloneNotSupportedException x) {
			throw new ReactionException(x);
		}
		boolean[] bondToSplitMap = new boolean[q.getBondCount()];
		int bondToSplitMol = -1;
		IBond b = null;
		boolean hasBondsToSplit = false;
		for (int i = 0; i < q.getBondCount(); i++) {
			b = q.getBond(i);
			bondToSplitMap[i] = false;
			if ((b.getOrder() == CDKConstants.BONDORDER_SINGLE)
					&& (b.getAtom(0) instanceof SymbolQueryAtom)
					|| (b.getAtom(1) instanceof SymbolQueryAtom)) {
				bondToSplitMap[i] = true;
				hasBondsToSplit = true;
			}
		}
		b = null;
		if (!hasBondsToSplit)
			throw new ReactionException(
					"Metabolize: Can't find the bond to break in the reaction pattern!");
		// double bestRatio = 0, ratio = 0;
		for (int i = 0; i < list.size(); i++) {
			List thisMap = (List) list.get(i);
			for (int j = 0; j < thisMap.size(); j++) {
				RMap rmap = (RMap) thisMap.get(j);
				// several bonds to split, will select the one connected to a
				// ring - not the only solution though
				for (int k = 0; k < bondToSplitMap.length; k++)
					if ((bondToSplitMap[k]) && (rmap.getId2() == k)) {
						b = mol.getBond(rmap.getId1());
						if ((bondToSplitMol == -1)
								|| ((b.getAtom(0)
										.getFlag(CDKConstants.ISINRING)) || (b
										.getAtom(1)
										.getFlag(CDKConstants.ISINRING)))) {
							logger.finer("Selecting split \t"
									+ Integer.toString(bondToSplitMol));
							bondToSplitMol = rmap.getId1();
						}

					}
			}
			if (bondToSplitMol != -1) {
				b = mol.getBond(bondToSplitMol);
				splitMetabolize1(mol, b, bondToSplitMol, builder);
				break;
			}
			// if (b!=null) break;
		}

		if (bondToSplitMol == -1)
			new ReactionException(
					"Metabolize: Can't find the bond to break in the reactant!");
		// b = mol.getBondAt(bondToSplitMol);
		/*
		 * Atom[] atoms = b.getAtoms();
		 * 
		 * //break the bond mol.removeElectronContainer(bondToSplitMol); for
		 * (int i=0; i <2; i++) { // add H and link it to each atom f the broken
		 * bond Atom h =
		 * SilentChemObjectBuilder.getInstance().newAtom(Elements.HYDROGEN);
		 * mol.addAtom(h); mol.addBond(new
		 * Bond(atoms[i],h,CDKConstants.BONDORDER_SINGLE)); }
		 */
		// finally separate the resulting molecules
		try {
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
			CDKHydrogenAdder.getInstance(mol.getBuilder()).addImplicitHydrogens(mol);
		} catch (Exception x) {
		}		
		IAtomContainerSet result = ConnectivityChecker
				.partitionIntoMolecules(mol);
		SimpleReactions.setResidualID(mol, result);
		return result;

	}

	private static void splitMetabolize(IAtomContainer mol, IBond b,
			IChemObjectBuilder builder)
	// , int bondToSplitMol)
	{
		IAtom[] atoms = new IAtom[b.getAtomCount()];
		for (int i = 0; i < b.getAtomCount(); i++)
			atoms[i] = b.getAtom(i);

		logger.finer("splitMetabolize: Break bond " + b.getAtom(0).getSymbol()
				+ " " + b.getAtom(1).getSymbol());
		// break the bond
		mol.removeBond(b);
		// N=N
		if ((atoms[0].getSymbol().equals("N"))
				&& (atoms[1].getSymbol().equals("N")))
			for (int i = 0; i < 2; i++) {
				// add H and link it to each atom f the broken bond
				IAtom h = builder.newInstance(IAtom.class, "H");
				mol.addAtom(h);
				mol.addBond(builder.newInstance(IBond.class, atoms[i], h,
						CDKConstants.BONDORDER_SINGLE));
				h = builder.newInstance(IAtom.class, "H");
				mol.addAtom(h);
				mol.addBond(builder.newInstance(IBond.class, atoms[i], h,
						CDKConstants.BONDORDER_SINGLE));

			}
		else if ((atoms[0].getSymbol().equals("C"))
				&& (atoms[1].getSymbol().equals("C"))) {
			IAtom h = builder.newInstance(IAtom.class, "H");
			mol.addAtom(h);
			mol.addBond(builder.newInstance(IBond.class, atoms[0], h,
					CDKConstants.BONDORDER_SINGLE));
			IAtom o = builder.newInstance(IAtom.class, "O");
			mol.addAtom(o);
			h = builder.newInstance(IAtom.class, "H");
			mol.addAtom(h);
			mol.addBond(builder.newInstance(IBond.class, o, h,
					CDKConstants.BONDORDER_SINGLE));
			mol.addBond(builder.newInstance(IBond.class, atoms[1], o,
					CDKConstants.BONDORDER_SINGLE));
		} else {
			IAtom nAtom = null;
			IAtom cAtom = null;
			for (int i = 0; i < 2; i++) {
				if (atoms[i].getSymbol().equals("N"))
					nAtom = atoms[i];
				if (atoms[i].getSymbol().equals("C"))
					cAtom = atoms[i];
			}
			IAtom h = builder.newInstance(IAtom.class, "H");
			mol.addAtom(h);
			mol.addBond(builder.newInstance(IBond.class, nAtom, h,
					CDKConstants.BONDORDER_SINGLE));
			IAtom o = builder.newInstance(IAtom.class, "O");
			mol.addAtom(o);
			mol.addBond(builder.newInstance(IBond.class, cAtom, o,
					CDKConstants.BONDORDER_DOUBLE));
		}
	}

	private static IAtomContainerSet metabolize(IAtomContainer a,
			QueryAtomContainer q, List list, IChemObjectBuilder builder)
			throws ReactionException {
		logger.fine("metabolize: \t" + q.getID());
		logger.finer("Molecule \t" + a.getID());
		IAtomContainer mol = null;
		try {
			mol = (IAtomContainer) a.clone();
		} catch (CloneNotSupportedException x) {
			throw new ReactionException(x);
		}
		int bondToSplitMap = -1;
		int bondToSplitMol = -1;
		IBond b = null;
		for (int i = 0; i < q.getBondCount(); i++) {
			b = q.getBond(i);
			if ((b.getOrder() == CDKConstants.BONDORDER_DOUBLE)
					&& (b.getAtom(0) instanceof SymbolQueryAtom)
					&& (b.getAtom(1) instanceof SymbolQueryAtom)) {
				bondToSplitMap = i;
				break;
			}
		}
		b = null;
		if (bondToSplitMap == -1)
			throw new ReactionException(
					"Metabolize: Can't find the bond to break in the reaction pattern!");
		List<IBond> bondsToBreak = new ArrayList<IBond>();
		for (int i = 0; i < list.size(); i++) {
			List thisMap = (List) list.get(i);
			for (int j = 0; j < thisMap.size(); j++) {
				RMap rmap = (RMap) thisMap.get(j);
				if (rmap.getId2() == bondToSplitMap) {
					b = mol.getBond(rmap.getId1());
					bondToSplitMol = rmap.getId1();
					// this is the bond to be broken
					logger.finer("Break bond " + b.hashCode() + " "
							+ b.getAtom(0).getSymbol() + " "
							+ b.getAtom(1).getSymbol());
					bondsToBreak.add(b);
					break;
				}
			}
			// if (b!=null) break;
		}
		for (int i = 0; i < bondsToBreak.size(); i++) {
			b = bondsToBreak.get(i);
			splitMetabolize(mol, b, builder);
		}

		if (b == null)
			new ReactionException(
					"Metabolize: Can't find the bond to break in the reactant!");

		// finally separate the resulting molecules
		try {
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
			CDKHydrogenAdder.getInstance(mol.getBuilder()).addImplicitHydrogens(mol);
		} catch (Exception x) {
		}
		IAtomContainerSet result = ConnectivityChecker
				.partitionIntoMolecules(mol);
		SimpleReactions.setResidualID(mol, result);
		return result;
	}

	public static void setResidualID(IAtomContainer mol, IAtomContainerSet r) {
		if (r == null)
			return;

		for (int i = 0; i < r.getAtomContainerCount(); i++)
			if (logger.isLoggable(Level.FINE)) {
				// try {
				r.getAtomContainer(i)
						.setID(gen.createSMILES((IAtomContainer) r
								.getAtomContainer(i))
								+ "\t'Residue "
								+ (i + 1)
								+ "'");
				/*
				 * } catch (CDKException x) { logger.error(x); }
				 */
			} else if (mol.getID() == null)
				r.getAtomContainer(i).setID("'Residue " + (i + 1) + "'");
			else
				r.getAtomContainer(i).setID(
						mol.getID() + "\t'Residue " + (i + 1) + "'");
	}

	public static QueryAtomContainer createQueryContainer(
			IAtomContainer container) {
		QueryAtomContainer queryContainer = new QueryAtomContainer(
				SilentChemObjectBuilder.getInstance());

		for (int i = 0; i < container.getAtomCount(); i++) {
			IAtom atom = container.getAtom(i);
			if (atom instanceof IPseudoAtom) {
				InverseSymbolSetQueryAtom a = new InverseSymbolSetQueryAtom(
						queryContainer.getBuilder());
				// a.setSymbol(atoms[i].getSymbol());
				// a.setAtomTypeName(atoms[i].getAtomTypeName());
				a.addSymbol("H");
				queryContainer.addAtom(a);
			} else
				queryContainer.addAtom(new SymbolQueryAtom(atom));
		}

		boolean onlyRXbonds = true;
		for (int i = 0; i < container.getBondCount(); i++) {
			IBond bond = container.getBond(i);
			if (!(bond.getAtom(0) instanceof IPseudoAtom)
					&& !(bond.getAtom(1) instanceof IPseudoAtom)) {
				onlyRXbonds = false; // a bond between two defined atoms
				break;
			}
		}

		for (int i = 0; i < container.getBondCount(); i++) {
			IBond bond = container.getBond(i);
			boolean anyBond = (bond.getAtom(0) instanceof IPseudoAtom)
					|| (bond.getAtom(1) instanceof IPseudoAtom);
			int index1 = container.getAtomNumber(bond.getAtom(0));
			int index2 = container.getAtomNumber(bond.getAtom(1));
			IQueryAtom atom1 = (IQueryAtom) queryContainer.getAtom(index1);
			IQueryAtom atom2 = (IQueryAtom) queryContainer.getAtom(index2);
			if (bond.getFlag(CDKConstants.ISAROMATIC)) {
				queryContainer.addBond(new AromaticQueryBond(
						(IQueryAtom) queryContainer.getAtom(index1),
						(IQueryAtom) queryContainer.getAtom(index2),
						IBond.Order.SINGLE, queryContainer.getBuilder()));
			} else {
				OrderQueryBond qBond = null;
				if (onlyRXbonds) {
					// logger.debug("Adding acyclic bond\t",atom1,atom2);
					qBond = new TopologyOrderQueryBond(atom1, atom2,
							bond.getOrder(), false, queryContainer.getBuilder());
				} else if (anyBond) {
					// logger.debug("Adding any bond\t");
					qBond = new OrderQueryBond(atom1, atom2, bond.getOrder(),
							queryContainer.getBuilder());

				} else {
					// logger.debug("Adding acyclic bond\t",atom1,atom2);
					qBond = new TopologyOrderQueryBond(atom1, atom2,
							bond.getOrder(), false, queryContainer.getBuilder());
				}

				queryContainer.addBond(qBond);

			}
		}
		return queryContainer;
	}

	public IAtomContainerSet hydrolizesPO4(IAtomContainer mol)
			throws ReactionException {
		return hydrolizesPO4(mol, mol.getBuilder());
	}

	public IAtomContainerSet hydrolizesPO4(IAtomContainer mol,
			IChemObjectBuilder builder) throws ReactionException {
		logger.finer("Hydrolizes PO4\t" + mol.getID());
		IAtomContainerSet result = null;
		for (int i = 0; i < getHydrolysisPO4ReactionCount(); i++)
			try {
				logger.finer("try1 " + i);
				IReaction r = getHydrolysisPO4Reaction(i);
				logger.finer("try2");
				result = process(mol, r, builder);
				logger.finer("try3");
				if (result != null) {
					logger.finer("YES, it is readily PO4-hydrolised by\t"
							+ r.getID());
					return result;
				}
			} catch (CDKException x) {
				logger.log(Level.SEVERE,
						"Error when trying PO4-hydrolysis reaction!", x);
				throw new ReactionException(x);
			}
		logger.finer("NO, it is NOT readily PO4-hydrolised (by predefined reactions)\t");
		return null;
	}

	public IAtomContainerSet isReadilyHydrolised(IAtomContainer mol)
			throws ReactionException {
		return isReadilyHydrolised(mol, mol.getBuilder());
	}

	/**
	 * Checks if 6 predefined hydrolysis reactions can be applied to Molecule
	 * mol
	 * 
	 * @param mol
	 * @return null if not readily hydrolised, otherwise returns hydrolysis
	 *         products
	 */
	public IAtomContainerSet isReadilyHydrolised(IAtomContainer mol,
			IChemObjectBuilder builder) throws ReactionException {
		logger.finer("Verifying if molecule is readily hydrolised\t"
				+ mol.getID());
		IAtomContainerSet result = null;
		for (int i = 0; i < getHydrolysisReactionCount(); i++)
			try {
				IReaction r = getHydrolysisReaction(i);
				result = process(mol, r, builder);
				if (result != null) {
					logger.fine("YES, it is readily hydrolised by\t"
							+ r.getID());
					return result;
				}
			} catch (CDKException x) {
				logger.log(Level.SEVERE,
						"Error when trying hydrolysis reaction!", x);
				throw new ReactionException(x);
			}
		logger.finer("NO, it is NOT readily hydrolised (by predefined reactions)\t");
		return null;
	}

	/**
	 * Checks if simple ester hydrolysis can be applied to Molecule mol
	 * 
	 * @param mol
	 * @return null if not readily hydrolised, otherwise returns hydrolysis
	 *         products
	 */

	public IAtomContainerSet isReadilyHydrolisedSimpleEster(IAtomContainer mol)
			throws ReactionException {
		return isReadilyHydrolisedSimpleEster(mol, mol.getBuilder());
	}

	public IAtomContainerSet isReadilyHydrolisedSimpleEster(IAtomContainer mol,
			IChemObjectBuilder builder) throws ReactionException {

		IReaction r = null;
		try {
			r = getHydrolysisReaction(0);
		} catch (CDKException x) {
			throw new ReactionException(x);
		}
		logger.finer("Verifying if molecule \t" + mol.getID()
				+ "\t is readily hydrolised by\t" + r.getID());
		IAtomContainerSet result = null;
		result = process(mol, r, builder);
		if (result != null) {
			logger.finer("YES, it is readily hydrolised by\t" + r.getID());
			return result;
		}
		return null;
	}

	public IAtomContainerSet canMetabolize(IAtomContainer mol,
			boolean allPossible) throws ReactionException {
		return canMetabolize(mol, allPossible,
				SilentChemObjectBuilder.getInstance());
	}

	/**
	 * Checks if 4 predefined metabolic reactions can be applied to Molecule mol
	 * 
	 * @param mol
	 * @return null if not metabolized by these, otherwise returns metabolic
	 *         products
	 */
	public IAtomContainerSet canMetabolize(IAtomContainer mol,
			boolean allPossible, IChemObjectBuilder builder)
			throws ReactionException {
		if (mol == null)
			throw new ReactionException("Null molecule");
		logger.finer("Verifying if molecule can be metabolised\t"
				+ mol.getAtomCount() + "\t" + mol.getID());
		IAtomContainerSet result = null;
		for (int i = 0; i < (getMetabolicReactionCount() - 1); i++)
			try {
				IReaction r = getMetabolicReaction(i);
				result = process(mol, r, builder);
				if (result != null) {
					logger.finer("YES, it can be metabolised by\t" + r.getID());
					IAtomContainerSet allProducts = null;
					if (allPossible)
						for (int p = 0; p < result.getAtomContainerCount(); p++) {
							IAtomContainer a = (IAtomContainer) result
									.getAtomContainer(p);
							if (logger.isLoggable(Level.FINE)) {
								a.setID(gen.createSMILES(a));
							}
							IAtomContainerSet newProducts = canMetabolize(a,
									allPossible, builder);
							if (allProducts == null)
								allProducts = new AtomContainerSet();

							if (newProducts == null)
								allProducts.addAtomContainer(a); // adding
							// the
							// original
							// residue
							else
								allProducts.add(newProducts);
						}

					if (allProducts == null)
						return result;
					else
						return allProducts;

				}
			} catch (CDKException x) {
				logger.log(Level.SEVERE,
						"Error when trying metabolic reaction!", x);
				throw new ReactionException(x);
			}
		logger.info("NO, this molecule can NOT be metabolized by predefined reactions");
		return null;
	}
}
