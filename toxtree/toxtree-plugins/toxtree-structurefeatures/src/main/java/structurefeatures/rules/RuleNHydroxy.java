package structurefeatures.rules;

import java.util.logging.Level;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;
public class RuleNHydroxy extends RuleSMARTSSubstructureAmbit {
	private static final long serialVersionUID = 0;
	public RuleNHydroxy() {
		super();		
		try{
			super.initSingleSMARTS(super.smartsPatterns,"1","a-NO");
			id = "32";
			title = "aromatic N-hydroxy amine";
			
			examples[0] = "";
			examples[1] = "";	
			editable = false;		
			
	} catch (SMARTSException x) {
		logger.log(Level.SEVERE,x.getMessage(),x);
	}

	}

}