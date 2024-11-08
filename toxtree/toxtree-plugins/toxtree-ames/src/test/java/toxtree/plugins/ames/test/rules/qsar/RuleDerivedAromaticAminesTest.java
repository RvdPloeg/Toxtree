/*
Copyright Ideaconsult Ltd. (C) 2005-2012 

Contact: jeliazkova.nina@gmail.com

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

package toxtree.plugins.ames.test.rules.qsar;

import java.util.ArrayList;
import java.util.Hashtable;

import junit.framework.Assert;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.smiles.SmilesGenerator;

import toxTree.core.IDecisionRule;
import toxTree.query.FunctionalGroups;
import toxTree.query.MolAnalyser;
import toxTree.query.MolFlags;
import toxtree.plugins.ames.rules.RuleDerivedAromaticAmines;
import toxtree.plugins.ames.test.TestAmesMutagenicityRules;

public class RuleDerivedAromaticAminesTest extends TestAmesMutagenicityRules {

	@Override
	protected IDecisionRule createRuleToTest() throws Exception {
		return new RuleDerivedAromaticAmines();
	}

	@Override
	public String getHitsFile() {
		return "DerivedAmines/hits.sdf";
	}

	@Override
	public String getResultsFolder() {
		return "DerivedAmines";
	}

	public void testDerivedAmine1() {
		try {
			// IAtomContainer c =
			// FunctionalGroups.createAtomContainer("COC=1C(N=C=C)=C(C=CC=1(N=C=O))C=2C=CC(N=C=O)=C(C=2)OC");
			// IAtomContainer c =
			// FunctionalGroups.createAtomContainer("[H]C([H])C=1C=CC(=CC=1(N=C=O))N=C=O.[H]C([H])C=1C(=CC=CC=1(N=C=O))N=C=O");
			IAtomContainer c = FunctionalGroups
					.createAtomContainer("[H]C([H])C=1C=CC(=CC=1(N=C))N=C=O.[H]C([H])C=1C(=CC=CC=1(N=C=O))N=C");
			// IAtomContainer c =
			// FunctionalGroups.createAtomContainer("c1ccccc1N=C",true);
			MolAnalyser.analyse(c);

			ruleToTest.verifyRule(c);
			MolFlags mf = (MolFlags) c.getProperty(MolFlags.MOLFLAGS);
			assertNotNull(mf);
			assertNotNull(mf.getResidues());
			IAtomContainerSet sc = mf.getResidues();
			SmilesGenerator g = SmilesGenerator.generic();
			ArrayList<String> results = new ArrayList<String>();
			results.add("[H]N([H])C=1C([H])=C([H])C([H])=C(C=1C([H])([H])[H])N([H])[H]");
			results.add("[H]N([H])C=1C([H])=C([H])C(=C(C=1([H]))N([H])[H])C([H])([H])[H]");
			results.add("[H]C([H])([H])[H]");
			/*
			 * results.add(
			 * "[H]NC=1C([H])=C([H])C(=C([H])C=1(OC([H])([H])[H]))C=2C([H])=C([H])C(N[H])=C(OC([H])([H])[H])C=2(N[H])"
			 * ); results.add("[H]C=C([H])[H]");
			 * results.add("[H]NC1=C([H])C([H])=C([H])C(N[H])=C1C([H])([H])[H]"
			 * );
			 * results.add("[H]NC=1C([H])=C([H])C(=C(N[H])C=1([H]))C([H])([H])[H]"
			 * );
			 */
			for (int i = 0; i < sc.getAtomContainerCount(); i++) {
				String s = g.createSMILES((IAtomContainer) sc
						.getAtomContainer(i));
				System.out.println("result " + s);
				assertTrue(results.indexOf(s) > -1);
			}

		} catch (Exception x) {
			x.printStackTrace();
			fail(x.getMessage());
		}
	}

	public void testDerivedAmine() throws Exception {

		IAtomContainer c = FunctionalGroups
				.createAtomContainer("COC=1C(N=C=C)=C(C=CC=1(N=C=O))C=2C=CC(N=C=O)=C(C=2)OC");
		// IAtomContainer c =
		// FunctionalGroups.createAtomContainer("[H]C([H])C=1C=CC(=CC=1(N=C=O))N=C=O.[H]C([H])C=1C(=CC=CC=1(N=C=O))N=C=O");

		// IAtomContainer c =
		// FunctionalGroups.createAtomContainer("c1ccc(N=C=O)cc1");
		// [H]C([H])C=1C=CC(=CC=1(N=C=O))N=C=O.[H]C([H])C=1C(=CC=CC=1(N=C=O))N=C=O

		MolAnalyser.analyse(c);

		IAtomContainerSet sc = ((RuleDerivedAromaticAmines) ruleToTest)
				.detachSubstituent(RuleDerivedAromaticAmines.group1(), c);
		assertNotNull(sc);
		Hashtable<String, Integer> results = new Hashtable<String, Integer>();
		results.put(
				"[H]NC=1C([H])=C([H])C(=C([H])C=1(OC([H])([H])[H]))C=2C([H])=C([H])C(N[H])=C(OC([H])([H])[H])C=2([H])",
				new Integer(14));
		results.put("O=C[H]", new Integer(1));
		if (sc != null) {
			SmilesGenerator g = SmilesGenerator.generic();
			for (int i = 0; i < sc.getAtomContainerCount(); i++) {
				String s = g.createSMILES((IAtomContainer) sc
						.getAtomContainer(i));
				System.out.println(s);
				// assertNotNull(results.get(s));
				// MFAnalyser mf = new MFAnalyser(sc.getAtomContainer(i));
				// assertEquals(results.get(s),new
				// Integer(mf.getAtomCount("C")));
				// System.out.println(FunctionalGroups.mapToString(sc.getAtomContainer(i)));
				// System.out.println(FunctionalGroups.hasGroupMarked(sc.getAtomContainer(i),q.getID()));
			}

		} else
			Assert.fail();
	}

}
