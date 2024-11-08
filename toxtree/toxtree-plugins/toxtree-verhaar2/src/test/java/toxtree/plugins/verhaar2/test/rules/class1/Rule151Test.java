/*
Copyright Ideaconsult Ltd. (C) 2005-2011

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

package toxtree.plugins.verhaar2.test.rules.class1;

import toxTree.core.IDecisionRule;
import toxtree.plugins.verhaar2.test.rules.AbstractRuleTest;
import verhaar.rules.Rule151;


public class Rule151Test extends AbstractRuleTest {
	@Override
	protected IDecisionRule createRule() throws Exception {
		return new Rule151();
	}
	public void test() throws Exception {
	    Object[][] answer = {
	            	{"C1OC1",Boolean.FALSE},
	            	{"COCCC",Boolean.TRUE},
	            	{"COOCC",Boolean.FALSE},
	            	{"C1OCC1",Boolean.TRUE},
	            	{"C1OCC1Cc1ccccc1",Boolean.TRUE},
	            	//ethers - BA
	            	{"[H]C([H])([H])OC(C([H])([H])[H])(C([H])([H])[H])C([H])([H])C(=O)C([H])([H])[H]", Boolean.TRUE},
	            	{"[H]OC([H])([H])C([H])([H])OC([H])([H])C([H])([H])OC([H])([H])C([H])([H])C([H])([H])C([H])([H])[H]", Boolean.TRUE},
	            	//{"[H]OC([H])([H])C([H])([H])C([H])([H])Cl", Boolean.TRUE},
	            	{"[H]OC([H])([H])C([H])([H])OC([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])[H]", Boolean.TRUE},
	            	{"[H]OC([H])([H])C([H])([H])Oc1c([H])c([H])c(c([H])c1([H]))C(C([H])([H])[H])(C([H])([H])[H])C([H])([H])C(C([H])([H])[H])(C([H])([H])[H])C([H])([H])[H]", Boolean.TRUE},
	            	{"[H]OC([H])([H])C([H])([H])OC([H])([H])C([H])([H])Oc1c([H])c([H])c(c([H])c1([H]))C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])C([H])([H])[H]", Boolean.TRUE},
	            	
	            	{"[H]c1c([H])c([H])c(C(=O)OC([H])([H])C([H])(C([H])([H])[H])C([H])([H])[H])c(c1([H]))C(=O)OC([H])([H])C([H])(C([H])([H])[H])C([H])([H])[H]",Boolean.FALSE}
	   
	    };
	    ruleTest(answer); 
	}	
	
	
}



