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
 * Created on 2005-5-2

 * @author Nina Jeliazkova nina@acad.bg
 *
 * Project : toxtree
 * Package : toxTree.tree.rules
 * Filename: RuleLactonesFusedOrUnsaturated.java
 */
package toxTree.tree.cramer;

import java.util.List;
import java.util.logging.Level;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;

import toxTree.exceptions.DecisionMethodException;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolFlags;
import toxTree.query.QueryAtomContainers;
import toxTree.tree.rules.RuleAnySubstructure;
import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.rendering.IAtomContainerHighlights;

/**
 * Rule 9 of the Cramer scheme (see {@link toxTree.tree.cramer.CramerRules})
 * @author Nina Jeliazkova <br>
 * @version 0.1, 2005-5-2
 */
public class RuleLactonesFusedOrUnsaturated extends RuleAnySubstructure {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7838501504594198746L;
	protected static transient QueryAtomContainer lactoneBreakable = null;
	protected static transient QueryAtomContainer lactoneUnsaturated = null;

	public RuleLactonesFusedOrUnsaturated() throws Exception {
		super();

		lactoneUnsaturated = FunctionalGroups.lactone(true);
		id = "9";
		title = "Lactone, fused to another ring, or 5- or 6-membered a,b-unsaturated lactone?";
		explanation.append("<html>Is it a ");
		explanation.append(title);
		explanation.append("<p>This places certain lactones known or suspected to be of unusual toxicity in class III.");
		explanation.append("<p>*If it is a lactone, from this point on treat the structure as if it were the hydroxy acid in the form of its more stable tautomer and proceed to Q20 if it is open chain,"); 
		explanation.append("to 10 if it is heterocyclic and to 23 if it is carboxylic; if it is a cyclic diester treat as the separate components");
		examples[0] = "O=C1CCC(O1)C2=CC=CC=C2";
		examples[1] = "CC1COC1=O";
		editable = false;
	}
	
	@Override
	protected QueryAtomContainers initQuery() throws Exception {
		QueryAtomContainers q = super.initQuery();
		setQuery(q);
		addSubstructure(FunctionalGroups.lactone(false));
		return q;
	}	
	/**
	 * {@link toxTree.core.IDecisionRule#verifyRule(IAtomContainer)}
	 */
	@Override
	public boolean verifyRule(IAtomContainer  mol) throws DecisionMethodException {
		logger.finer(toString());
		List list = FunctionalGroups.getBondMap(mol,(QueryAtomContainer)getSubstructure(0),false);
		if  (list.size() > 0) {
			boolean FusedOrUnsaturated = false;
			FunctionalGroups.markMaps(mol,(QueryAtomContainer)getSubstructure(0),list);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Lactone found");
				logger.fine(FunctionalGroups.mapToString(mol).toString());
			}
		    MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
		    if (mf == null) throw new DecisionMethodException(ERR_STRUCTURENOTPREPROCESSED);
		    IRingSet rings = mf.getRingset();
		    if (rings.getAtomContainerCount() == 1) { //single ring
		    	IRing ring = (IRing)rings.getAtomContainer(0);
		    	int ac = ring.getAtomCount();
		    	if ((ac >= 5) && (ac<=6)) {
		    		logger.fine("Single ring found, 5- or 6- membered");
		    		for (int i=0; i<ring.getBondCount(); i++) 
		    			if ((ring.getBond(i).getOrder() == CDKConstants.BONDORDER_DOUBLE) ||
		    				(ring.getBond(i).getOrder() == CDKConstants.BONDORDER_TRIPLE)) {
		    				logger.fine("Unsaturated ring found");
		    				
		    				if (FunctionalGroups.hasGroup(mol,lactoneUnsaturated)) {
		    					logger.fine("alpha beta unsaturated lactone\tYES");
		    					FusedOrUnsaturated = true;
		    					break;
		    				} else logger.fine("alpha beta unsaturated lactone\tNO");
		    			}
		    		logger.fine("Unsaturated ring NOT found");
		    	} else {
		    		logger.fine("Single ring found, but NOT 5- or 6- membered");
		    	}
		    } else { 
		    	logger.fine("More than one ring, will check if fused");
		    	//find lactone ring bond, it should has property FunctionalGroups.LACTONE set

		    	IRing lactoneRing = null;
		    	for (int i=0; i< rings.getAtomContainerCount(); i++) {
		    		IRing ring = (IRing)rings.getAtomContainer(i);
		    		for (int j=0; j< ring.getBondCount(); j++) {
		    			IBond b = ring.getBond(j);
		    			Object o = b.getProperty(FunctionalGroups.LACTONE);
		    			if (o != null) { //this is it , we have found which ring is lactone
		    				lactoneRing = ring;
		    				break;
		    			}
		    		}
		    	}
		    	if (lactoneRing == null) throw new DecisionMethodException("Can't find C-O bond of the lactone!!!!");
		    	IRingSet fusedRings = rings.getConnectedRings(lactoneRing); 
		    	//hmm, actually may not be fused, but just have a single atom in common ... will skip that for now
		    	if (fusedRings.getAtomContainerCount()==0) {
		    		logger.fine("No ring is fused to the lactone ring.");
		    		fusedRings = null;
		    		return false;
		    	} else {
		    		logger.fine("A ring fused to the lactone ring FOUND.");
		    		fusedRings = null;
    				FusedOrUnsaturated = true;
		    	}
		    }
			;
			FunctionalGroups.clearMark(mol,FunctionalGroups.LACTONE);
			if (lactoneBreakable == null) lactoneBreakable = FunctionalGroups.lactoneBreakable();
			
			IAtomContainer a = null;
			try {
			a = (IAtomContainer) mol.clone();
			} catch (CloneNotSupportedException x) {
			    throw new DecisionMethodException(x);
			}
			IAtomContainerSet hydroxyAcid = FunctionalGroups.detachGroup(a,lactoneBreakable);
			hydroxyAcid.setID("Hydroxy acid ");
			mf.setResidues(hydroxyAcid);
			
		    return FusedOrUnsaturated;
		    //will 
		} else {
			logger.fine("Not a lactone");
			return false;
		}
	}
	
    @Override
    public IAtomContainerHighlights getSelector() {
    	RuleSMARTSSubstructureAmbit rule = new RuleSMARTSSubstructureAmbit();
    	try { rule.addSubstructure("O@C(=[O;R0])"); } catch (Exception x) {x.printStackTrace();};
    	return rule.getSelector();
    }

}
