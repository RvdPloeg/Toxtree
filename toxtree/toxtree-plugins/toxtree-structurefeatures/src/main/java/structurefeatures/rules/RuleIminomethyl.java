package structurefeatures.rules;

import java.util.logging.Level;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;
public class RuleIminomethyl extends RuleSMARTSSubstructureAmbit {
	private static final long serialVersionUID = 0;
	public RuleIminomethyl() {
		super();		
		try {
			super.initSingleSMARTS(super.smartsPatterns,"1", "[nr5]:[cX3H]");			
			id = "14";
			title = "Iminomethyl";
			
			examples[0] = "";
			examples[1] = "";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}

	}

}
