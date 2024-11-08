package toxTree.test.smarts;


import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SmartsPatternAmbit;

/**
 * Tests for {@link ambit2.smarts.SmartsManager} and derived rules.
 * @author nina
 *
 */
public class TestNKSmartsSearch
{
	@Test
	public void testSmartsPatternAmbit() throws Exception  {
		SmartsPatternAmbit sp = new SmartsPatternAmbit("[$(*O);$(*CC)]");
		IAtomContainer c = FunctionalGroups.createAtomContainer("OCCC");
		Assert.assertTrue(sp.hasSMARTSPattern(c)>0);
	}
	@Test
	public void testSmartsStructureAmbit() throws Exception  {
		RuleSMARTSSubstructureAmbit sp = new RuleSMARTSSubstructureAmbit();
		sp.addSubstructure("[$(*O);$(*CC)]");
		IAtomContainer c = FunctionalGroups.createAtomContainer("OCCC");
		MolAnalyser.analyse(c);
		Assert.assertTrue(sp.verifyRule(c));
	}	
	/*
	public void testSmartsAlertAmbit() throws Exception  {
		StructureAlertAmbit sp = new StructureAlertAmbit();
		sp.addSubstructure("[$(*O);$(*CC)]");
		IAtomContainer c = FunctionalGroups.createAtomContainer("CN(C)C(=O)Cl");
		MolAnalyser.analyse(c);
		assertTrue(sp.verifyRule(c));
	}
	*/
	@Test
	public void testSmartsAlertAmbit1() throws Exception  {
		RuleSMARTSSubstructureAmbit sp = new RuleSMARTSSubstructureAmbit();
		sp.addSubstructure("[!$([OH1,SH1])]C(=O)[Br,Cl,F,I]");
		IAtomContainer c = FunctionalGroups.createAtomContainer("OCCC");
		MolAnalyser.analyse(c);
		Assert.assertTrue(sp.verifyRule(c));
	}		
}
