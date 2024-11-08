package toxtree.plugins.smartcyp.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.FixBondOrdersTool;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import toxTree.core.IMetaboliteGenerator;
import toxTree.tree.AbstractRule;
import toxTree.tree.RuleResult;
import ambit2.smarts.IAcceptable;
import ambit2.smarts.SMIRKSManager;
import ambit2.smarts.SMIRKSReaction;
import ambit2.smarts.SmartsConst;
import dk.smartcyp.core.MoleculeKU.SMARTCYP_PROPERTY;
import dk.smartcyp.core.SMARTSData;
import dk.smartcyp.smirks.SMARTCYPReaction;

public abstract class MetaboliteGenerator extends AbstractRule implements
		IMetaboliteGenerator, IAcceptable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3202810538351239202L;
	protected transient SMIRKSManager smrkMan;
	protected int rank;
	protected transient ResourceBundle bundle;
	
	public MetaboliteGenerator() {
		super();
		bundle = ResourceBundle.getBundle(getClass().getName(),Locale.ENGLISH,getClass().getClassLoader());
	}
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public IAtomContainerSet getProducts(IAtomContainer reactant,
			RuleResult ruleResult) throws Exception {
		IAtomContainerSet products = null;
	//	AtomConfigurator cfg = new AtomConfigurator();
		if (smrkMan == null) {
			smrkMan = new SMIRKSManager(SilentChemObjectBuilder.getInstance());
			//smrkMan.setSSMode(SmartsConst.SSM_NON_IDENTICAL_FIRST);
			smrkMan.setSSMode(SmartsConst.SSM_MODE.SSM_NON_IDENTICAL);
			smrkMan.setFlagFilterEquivalentMappings(true);
		}
		List<SMARTCYPReaction> reactions = new ArrayList<SMARTCYPReaction>();
		for (IAtom atom : reactant.atoms()) {

			
			Number atom_rank = SMARTCYP_PROPERTY.Ranking.getNumber(atom);
			if (atom_rank == null)
				continue;
			if (atom_rank.intValue() != getRank())
				continue;
			
			SMARTSData data = SMARTCYP_PROPERTY.Energy.getData(atom);
			if (data == null) {
				if (products == null)
					products = SilentChemObjectBuilder.getInstance()
							.newInstance(IAtomContainerSet.class);
				//IAtomContainer product = NoNotificationChemObjectBuilder.getInstance().newInstance(IAtomContainer.class);
				//product.setID(String.format("No energy for rank 1 atom!"));
				//products.addAtomContainer(product);
				continue;
			}
				//throw new Exception("Energy property missing for atom of rank "		+ atom_rank);
			if (reactions.indexOf(data.getReaction()) < 0)
				reactions.add(data.getReaction());
		}

		for (SMARTCYPReaction reaction : reactions) {

			SMIRKSReaction smr = smrkMan.parse(reaction.getSMIRKS());

			IAtomContainer product = reactant; //(IAtomContainer) reactant.clone();
			IAtomContainerSet rproducts = smrkMan.applyTransformationWithSingleCopyForEachPos(product, this, smr);
			
			if (rproducts!=null) {
			//if (smrkMan.applyTransformation(product, this, smr)) {
				
				if (products == null)
					products = SilentChemObjectBuilder.getInstance().newInstance(IAtomContainerSet.class);
				
				FixBondOrdersTool fbt = new FixBondOrdersTool();
				for (IAtomContainer ac : rproducts.atomContainers()) {
					ac.setID(reaction.toString());
					
					for (IAtom atom: ac.atoms()) {

						atom.setFormalNeighbourCount((Integer) CDKConstants.UNSET);
					      atom.setAtomTypeName((String) CDKConstants.UNSET);
				            atom.setMaxBondOrder((IBond.Order) CDKConstants.UNSET);
				            atom.setBondOrderSum((Double) CDKConstants.UNSET);
				            atom.setHybridization((IAtomType.Hybridization) CDKConstants.UNSET);
					 //  atom.setBondOrderSum(null);
					  // atom.setValency(null);
					   
					   atom.setFlag(CDKConstants.ISAROMATIC, false);

						/*
					      atom.setAtomTypeName((String) CDKConstants.UNSET);
				            atom.setMaxBondOrder((IBond.Order) CDKConstants.UNSET);
				            atom.setBondOrderSum((Double) CDKConstants.UNSET);
				            atom.setCovalentRadius((Double) CDKConstants.UNSET);
				            atom.setValency((Integer) CDKConstants.UNSET);
				           // atom.setFormalCharge((Integer) CDKConstants.UNSET);
				            atom.setHybridization((IAtomType.Hybridization) CDKConstants.UNSET);
				            atom.setFormalNeighbourCount((Integer) CDKConstants.UNSET);
				            atom.setFlag(CDKConstants.IS_HYDROGENBOND_ACCEPTOR, false);
				            atom.setFlag(CDKConstants.IS_HYDROGENBOND_DONOR, false);
				            atom.setProperty(CDKConstants.CHEMICAL_GROUP_CONSTANT, CDKConstants.UNSET);
				            atom.setFlag(CDKConstants.ISAROMATIC, false);
				            atom.setProperty("org.openscience.cdk.renderer.color", CDKConstants.UNSET);
				            atom.setAtomicNumber((Integer) CDKConstants.UNSET);
				            atom.setExactMass((Double) CDKConstants.UNSET);			
				            */			
					}
					for (IBond bond : ac.bonds()) {
						bond.setFlag(CDKConstants.ISAROMATIC, false);
					}
					//AtomContainerManipulator.clearAtomConfigurations(ac); //not all!
					AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(ac);
					//if (ac instanceof IMolecule)
					//	ac = fbt.kekuliseAromaticRings((IMolecule)ac);
					//AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(ac);
					//CDKHueckelAromaticityDetector.detectAromaticity(ac);
					
				}
				products.add(rproducts);
				//product.setID(reaction.toString());
	
				//products.addAtomContainer(product);
			} else {
				if (products == null)
					products = SilentChemObjectBuilder.getInstance()
							.newInstance(IAtomContainerSet.class);
				product = SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class);
				product.setID(String.format("Can't generate products! <br>%s<br>%s<br>%s",
						reaction.name(),
						reaction.getSMIRKS(),
						smrkMan.getErrors()));
				products.addAtomContainer(product);
			}
		}

		return products;
	}

	@Override
	public boolean accept(List<IAtom> atoms) {

		boolean ok = false;
		for (IAtom atom : atoms) {
			Number atom_rank = SMARTCYP_PROPERTY.Ranking.getNumber(atom);
			if (atom_rank == null)
				continue;
			if (atom_rank.intValue() == getRank())
				ok = true; // any atom with rank 1 within this mapping
		}
		return ok;
	}

	@Override
	public String getHelp(RuleResult ruleresult) {
		return bundle.getString("metabolite_help");
	}
}
