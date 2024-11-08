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

 *//**
 * Created on 2005-5-3

 * @author Nina Jeliazkova nina@acad.bg
 *
 * Project : toxtree
 * Package : toxTree.tree.rules
 * Filename: RuleSomeFuncGroups.java
 */
package toxTree.tree.cramer;

import java.util.List;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import toxTree.exceptions.DecisionMethodException;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolFlags;
import toxTree.query.QueryAtomContainers;
import toxTree.tree.rules.RuleRingSubstituents;
import ambit2.core.data.MoleculeTools;

/**
 * Rule 32 of the Cramer scheme (see {@link toxTree.tree.cramer.CramerRules})
 * 
 * @author Nina Jeliazkova <br>
 * @version 0.1, 2005-5-3
 */
public class RuleOnlyFuncGroupsQ30 extends RuleRingSubstituents {
	protected int index_polyoxyethylene = 8;
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3457260128790459206L;

	/**
	 * Constructor
	 * 
	 */
	public RuleOnlyFuncGroupsQ30() throws Exception {
		super();
		id = "32";
		title = "Contains only the functional groups listed in Q30 or Q31 and those listed below.";
		explanation.append("<html>");
		explanation
				.append("Does the substance contain only the <i>functional groups</i> (E) listed in Q30, or their derivatives listed in Q31, but with any or all of the following:<UL>");
		explanation
				.append("<LI>(a)a single fused non-aromatic carbocyclic ring");
		explanation
				.append("<LI>(b)Ii>aliphatic</i> (A) substituent chains longer than 5 carbon atoms, or");
		explanation
				.append("<LI>(c)a polyoxyethylene [(-OCH2CH2-)x, with x <= 4] chain either on the aromatic ring or on an aliphatic side chain?");
		explanation.append("</UL>");
		explanation
				.append("<P>Part (a) is intended to allow simple derivatives of tetralin into class II while putting polyciclic compounds such as steroids ultimately into class III except those that may be normal food components.");

		explanation
				.append("<p>Part (b) allows compounds with permitted functional groups but longer side chains into class II instead of sending them eventually into class III.");
		explanation
				.append("<p>Part (c) puts short-chain polyoxyethylene derivatives of aryl compounds into class II rather than class III.");
		explanation.append("</html>");
		examples[0] = "CSC1=CC=CC=C1";
		examples[1] = "CC(C)CCC(=O)C1=CC=CC=C1";
		editable = false;
	}

	@Override
	protected QueryAtomContainers initQuery() throws Exception {
		QueryAtomContainers q = super.initQuery();
		setQuery(q);
		addSubstructure(FunctionalGroups.hydroxy_ring());
		addSubstructure(FunctionalGroups.methoxy_ring());
		addSubstructure(FunctionalGroups.alcohol(false));
		addSubstructure(FunctionalGroups.ketone());
		addSubstructure(FunctionalGroups.aldehyde());
		addSubstructure(FunctionalGroups.carboxylicAcid());
		addSubstructure(FunctionalGroups.ester());
		addSubstructure(FunctionalGroups.acyclic_acetal());
		addSubstructure(FunctionalGroups.polyoxyethylene(1));
		return q;
	}

	protected boolean singleFusedNonAromaticCarbocyclicRing(IAtomContainer mol,
			IRingSet rings) {
		final String c = "C";
		if (FunctionalGroups.singleFusedRing(mol, rings)) {
			for (int i = 0; i < rings.getAtomContainerCount(); i++) {
				IRing ring = (IRing) rings.getAtomContainer(i);
				if (ring.getFlag(CDKConstants.ISAROMATIC)) {
					logger.fine("Aromatic ring found");
					return false;
				}
				for (int j = 0; j < ring.getAtomCount(); j++)
					if (!ring.getAtom(j).getSymbol().equals(c)) {
						logger.fine("Heteroring found\t"
								+ ring.getAtom(j).getSymbol());
						return false;
					}
			}
			//
			return true;
		} else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxTree.tree.rules.RuleRingSubstituents#substituentIsAllowed(org.openscience
	 * .cdk.AtomContainer)
	 */
	@Override
	public boolean substituentIsAllowed(IAtomContainer a, int[] place)
			throws DecisionMethodException {
		// aliphatic chain > 5 carbons

		IRingSet rs = Cycles.sssr(a).toRingSet();
		boolean b = false;
		if (rs.getAtomContainerCount() > 0) {
			logger.finer(CYCLIC_SUBSTITUENT);
			b = false;
		} else {
			IMolecularFormula formula = MolecularFormulaManipulator
					.getMolecularFormula(a);
			int c = MolecularFormulaManipulator.getElementCount(formula,
					MoleculeTools.newElement(formula.getBuilder(), "C"));

			if (c > 5) {
				logger.finer(LONGCHAIN_SUBSTITUENT + c);
				b = true;
			}
		}
		if (!b) { // polyoxyethylene check
			List list = FunctionalGroups.getUniqueBondMap(a,
					getSubstructure(index_polyoxyethylene), false);
			if (list != null) {
				if ((list.size() > 0) && (list.get(0) != null))
					logger.finer(getSubstructure(index_polyoxyethylene).getID()
							+ "\t" + Integer.toString(list.size()));

				if (list.size() > 4)
					b = false;
				else if (list.size() > 0)
					b = true;
			}
			list = null;
		}
		rs = null;
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxTree.tree.rules.RuleRingSubstituents#verifyRule(org.openscience.cdk
	 * .Molecule)
	 */
	@Override
	public boolean verifyRule(IAtomContainer mol)
			throws DecisionMethodException {
		return verifyRule(mol, null);
	};

	@Override
	public boolean verifyRule(IAtomContainer mol, IAtomContainer selected)
			throws DecisionMethodException {
		logger.finer(toString());
		MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
		if (mf == null)
			throw new DecisionMethodException(ERR_STRUCTURENOTPREPROCESSED);
		IRingSet rings = mf.getRingset();
		if (singleFusedNonAromaticCarbocyclicRing(mol, rings)) {
			logger.finer("Single Fused NonAromatic CarbocyclicRing\tYES");
			return true;
		}
		return super.verifyRule(mol, selected);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxTree.tree.rules.RuleSubstructures#isImplemented()
	 */
	@Override
	public boolean isImplemented() {
		return true;
	}
}
