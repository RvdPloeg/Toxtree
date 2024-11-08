package toxtree.plugin.kroes.test;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;

import toxtree.plugins.kroes.rules.KroesRule10;

public class KroesRule10Test {

		KroesRule10 rule = new KroesRule10();
		@Test
		public void testYes() throws Exception {
			IAtomContainer mol = rule.getExampleMolecule(true);
			Assert.assertTrue(rule.verifyRule(mol));
		}
		@Test
		public void testNo() throws Exception {
			IAtomContainer mol = rule.getExampleMolecule(false);
			Assert.assertFalse(rule.verifyRule(mol));
		}
		
}
