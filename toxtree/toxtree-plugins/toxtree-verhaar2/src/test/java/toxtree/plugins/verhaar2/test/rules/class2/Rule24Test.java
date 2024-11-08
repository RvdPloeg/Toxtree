package toxtree.plugins.verhaar2.test.rules.class2;

import toxTree.core.IDecisionRule;
import toxtree.plugins.verhaar2.test.rules.AbstractRuleTest;
import verhaar.rules.Rule24;

public class Rule24Test extends AbstractRuleTest {

	@Override
	protected IDecisionRule createRule() throws Exception {
		return new Rule24();
	}

	@Override
	public void test() throws Exception {
		   Object[][] answer = {
	            	{"CCCN",Boolean.TRUE},
	            	{"Cc1ccccc1CCN",Boolean.FALSE},
	            	{"CCCN(C)",Boolean.FALSE},
	            	{"COCCN",Boolean.FALSE},
	            	{"COCCN(C)(C)",Boolean.FALSE},
	            	{"CC(C)=CC=C(C)C",Boolean.FALSE}
		    };
		    
		    ruleTest(answer); 		
	}

}
