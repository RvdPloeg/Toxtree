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
package toxTree.cramer;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;

import toxTree.exceptions.DecisionMethodException;
import toxTree.exceptions.MolAnalyseException;
import toxTree.query.MolAnalyser;
import toxTree.tree.AbstractTree;
import toxTree.tree.cramer.CramerRules;

/**
 * TODO Add CramerRulesTest description
 * @author Nina Jeliazkova <br>
 * @version 0.1, 2005-4-30
 */
public class CramerRulesTest extends RulesTestCase {
    protected SmilesParser gen = null;
    
	/**
	 * 
	 */
	public CramerRulesTest() {
		super();
		try {
			rules = new CramerRules();
			((CramerRules)rules).setResiduesIDVisible(false);
		} catch (DecisionMethodException x) {
			Assert.fail();
		}	
        gen = new SmilesParser(SilentChemObjectBuilder.getInstance());
        
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testVerifyRule2() throws Exception {
		IAtomContainer mol = MoleculeFactory.makeAlkane(6);
		verifyRule(mol,2);
	}

	/*
	 * Class under test for int classify(IAtomContainer)
	 */
	@Test
	public void testClassifyMolecule() {
		IAtomContainer mol = MoleculeFactory.makeAlkane(6);
		classify(mol,rules,3);

	}

    protected IAtomContainer getMolecule(String smiles) {
        try {
        	IAtomContainer mol = gen.parseSmiles(smiles);
            MolAnalyser.analyse(mol);
            return mol;
	    } catch (InvalidSmilesException x ) {
	        x.printStackTrace();
	        return null;
	    } catch (MolAnalyseException x) {
	        x.printStackTrace();
	        return null;
	    }    
    }
    @Test
	public void testCramer() throws Exception {
		CramerRules rulesNew = (CramerRules)objectRoundTrip(rules,"CramerRules");
		rulesNew.setResiduesIDVisible(false);
		rules = rulesNew;
		tryImplementedRules();
	}
    @Test
	public void testPrintCramer() throws Exception {
		System.out.println(new CramerRules().getRules());

	}
	@Test
	public void testRulesWithSelector() throws Exception {
	    int na = ((AbstractTree)rules).testRulesWithSelector();
	 
	    Assert.assertEquals(0,na);
	}
}
