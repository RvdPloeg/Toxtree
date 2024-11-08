package structurefeatures.rules;

import java.util.logging.Level;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;
public class RuleEther extends RuleSMARTSSubstructureAmbit {
	private static final long serialVersionUID = 0;
	public RuleEther() {
		super();		
		try {
			super.initSingleSMARTS(super.smartsPatterns,"1", "[OD2]([#6])[#6]");			
			id = "8";
			title = "Ether";
			
			examples[0] = "";
			examples[1] = "";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}

	}

}