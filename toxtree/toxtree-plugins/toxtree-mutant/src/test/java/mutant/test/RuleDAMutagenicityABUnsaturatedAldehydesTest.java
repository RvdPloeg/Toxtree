/*
Copyright Ideaconsult Ltd. (C) 2005-2007  

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

package mutant.test;

import java.util.List;

import junit.framework.TestCase;
import mutant.rules.RuleDAMutagenicityABUnsaturatedAldehydes;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.qsar.IMolecularDescriptor;

import toxTree.query.FunctionalGroups;

public class RuleDAMutagenicityABUnsaturatedAldehydesTest extends TestCase {

	public void testRuleDAMutagenicityABUnsaturatedAldehydes() {
		try {
			RuleDAMutagenicityABUnsaturatedAldehydes rule = new RuleDAMutagenicityABUnsaturatedAldehydes();
			IAtomContainer acrolein = FunctionalGroups.createAtomContainer("C=CC=O", true);
			acrolein.setProperty("MR",1.655);
			acrolein.setProperty("logP",-0.01);
			acrolein.setProperty("LUMO",-0.15105);
			
			assertTrue(rule.verifyRule(acrolein));
				
		} catch (Exception x) {
			fail(x.getMessage());
		}
	}
	
	public void testHasDescriptors() {
		RuleDAMutagenicityABUnsaturatedAldehydes rule = new RuleDAMutagenicityABUnsaturatedAldehydes();
		List<IMolecularDescriptor> d = rule.getModel().getDescriptors();
		assertNotNull(d);
		assertEquals(3,d.size());
	}
	public void testDescriptorMRImplemented() {
		RuleDAMutagenicityABUnsaturatedAldehydes rule = new RuleDAMutagenicityABUnsaturatedAldehydes();
		List<IMolecularDescriptor> d = rule.getModel().getDescriptors();
		assertNotNull(d);
		assertNotNull(d.get(0));
	}
	public void testDescriptorLogPImplemented() {
		RuleDAMutagenicityABUnsaturatedAldehydes rule = new RuleDAMutagenicityABUnsaturatedAldehydes();
		List<IMolecularDescriptor> d = rule.getModel().getDescriptors();
		assertNotNull(d);
		assertNotNull(d.get(1));
	}
	public void testDescriptorELUMOImplemented() {
		RuleDAMutagenicityABUnsaturatedAldehydes rule = new RuleDAMutagenicityABUnsaturatedAldehydes();
		List<IMolecularDescriptor> d = rule.getModel().getDescriptors();
		assertNotNull(d);
		assertNotNull(d.get(2));
	}
	
	
	

	
}


