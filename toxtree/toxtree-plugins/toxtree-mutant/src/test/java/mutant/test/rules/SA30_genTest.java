package mutant.test.rules;

import junit.framework.Assert;
import mutant.rules.SA30_gen;
import mutant.test.TestMutantRules;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import toxTree.core.IDecisionRule;
import toxTree.query.MolAnalyser;

public class SA30_genTest extends TestMutantRules {

	@Override
	protected IDecisionRule createRuleToTest() throws Exception {
		return new SA30_gen();
	}
	@Override
	public String getHitsFile() {
		return "NA30/onc_16.sdf";

	}
	@Override
	public String getResultsFolder() {
		return "NA30";
		
	}
	
	/**
	 * https://sourceforge.net/tracker/?func=detail&aid=3138566&group_id=152702&atid=785126
	 */
	public void test_bug3138566() throws Exception {
		SmilesParser p = new SmilesParser(SilentChemObjectBuilder.getInstance());
		IAtomContainer m = p.parseSmiles("O=C1\\C=C/c2ccccc2O1");
		verifyExample(m, true);
		
	}
	
	/**
	 * Coumarin heteroring is not recognized as aromatic
	 * https://sourceforge.net/tracker/?func=detail&aid=3138566&group_id=152702&atid=785126
	 */
	public void test_bug3138566_coumarin_aromaticity() throws Exception {
		SmilesParser p = new SmilesParser(SilentChemObjectBuilder.getInstance());
		IAtomContainer m = p.parseSmiles("O=C1\\C=C/c2ccccc2O1");
		MolAnalyser.analyse(m);
		int aromatic = 0;
		for (IAtom a : m.atoms()) {
			aromatic += a.getFlag(CDKConstants.ISAROMATIC)?1:0;
		}
		//OK, the heteroring is not aromatic indeed 
		Assert.assertEquals(6,aromatic);
		
	}
}
