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
package toxTree.tree.rules;

import org.openscience.cdk.interfaces.IAtomContainer;

import ambit2.rendering.IAtomContainerHighlights;
import toxTree.exceptions.DecisionMethodException;
import toxTree.query.MolFlags;
import toxTree.tree.AbstractRule;
import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;

/**
 * Verifies if the moleucle is open chain
 * @author Nina Jeliazkova <br>
 * @version 0.1, 2005-5-2
 */
public class RuleOpenChain extends AbstractRule {

	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -6401742853134220208L;
    /**
	 * Constructor
	 * 
	 */
	public RuleOpenChain() {
		super();
		id = "[Open chain]";
		title = "Open chain";
		explanation.append("<html>Is the substance <i>open chain</i>?<p>");
		
		examples[0] = "OC(=O)CCCCCCCCCCCC1CCC=C1";
		examples[1] = "CC(C)=O";			
	}
	/**
	 * {@link toxTree.core.IDecisionRule#verifyRule(IAtomContainer)}
	 */
	public boolean verifyRule(IAtomContainer  mol) throws DecisionMethodException {
	    MolFlags mf = (MolFlags) mol.getProperty(MolFlags.MOLFLAGS);
	    if (mf ==null) throw new DecisionMethodException(ERR_STRUCTURENOTPREPROCESSED);
	    return  mf.isOpenChain();	    
	}
	/* (non-Javadoc)
     * @see toxTree.tree.AbstractRule#isImplemented()
     */
    @Override
	public boolean isImplemented() {
        return true;
    }	
    
    @Override
    public IAtomContainerHighlights getSelector() {
    	RuleSMARTSSubstructureAmbit rule = new RuleSMARTSSubstructureAmbit();
    	try { rule.addSubstructure("[R0;!#1]!@[R0;!#1]"); } catch (Exception x) {x.printStackTrace();};
    	return rule.getSelector();
    }
}
