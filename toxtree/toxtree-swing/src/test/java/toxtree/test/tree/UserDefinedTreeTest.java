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
package toxtree.test.tree;

import java.util.Observable;
import java.util.Observer;

import junit.framework.Assert;

import org.junit.Test;

import toxTree.core.IDecisionCategories;
import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionRule;
import toxTree.core.IDecisionRuleList;
import toxTree.tree.DecisionNode;
import toxTree.tree.DefaultCategory;
import toxTree.tree.UserDefinedTree;
import toxTree.tree.rules.RuleAnySubstructure;
import toxTree.tree.rules.RuleElements;
import toxtree.ui.tree.TreeLayout;

/**
 * Test for {@link UserDefinedTree}
 * @author Nina Jeliazkova
 * <b>Modified</b> 2005-10-7
 */
public class UserDefinedTreeTest {
	public int count = 0;
	
	@Test
	public void test() throws Exception {
		count = 0;
		UserDefinedTree tree = new UserDefinedTree();
		tree.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				IDecisionMethod tree = (IDecisionMethod) o;
				if (tree.getRules().size()>0) {
					count++;
				}
			}
		});
		IDecisionRuleList rules = tree.getRules();
		DecisionNode rule1 = new DecisionNode(new RuleAnySubstructure());
		rules.add(rule1);
		IDecisionRule rule2 = new DecisionNode(new RuleElements());
		rules.add(rule2);
		rule1.setBranch(true,rule2);
		Assert.assertEquals(3,count);
	}
	@Test
	public void testTreeLayout() throws Exception {
		count = 0;
		UserDefinedTree tree = new UserDefinedTree();
		TreeLayout treeLayout = new TreeLayout(tree);
		
		treeLayout.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				
				IDecisionMethod tree = ((TreeLayout) o).getMethod();
				if (tree.getRules().size()>0) {
					count++;
					//System.out.println(tree.getRules().size());
				}
			}
		});
		IDecisionRuleList rules = tree.getRules();
		IDecisionCategories categories = tree.getCategories();
		IDecisionCategory c1 = new DefaultCategory("First",1);
		IDecisionCategory c2 = new DefaultCategory("Second",2);
		categories.addCategory(c1);
		categories.addCategory(c2);
		
		DecisionNode node1 = new DecisionNode(new RuleAnySubstructure(),null,null,c1,c2);
		rules.add(node1);
		DecisionNode node2 = new DecisionNode(new RuleElements(),null,null,c1,c2); 
		rules.add(node2);
		node1.setBranch(false,node2);
		Assert.assertEquals(3,count);		
	}
	
}
