package structurefeatures.rules;

import java.util.logging.Level;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;
public class RulePhosphoricGroups extends RuleSMARTSSubstructureAmbit {
	private static final long serialVersionUID = 0;
	public RulePhosphoricGroups() {
		super();		
		try {
			//Phosphoric_acid groups
			super.initSingleSMARTS(super.smartsPatterns,"1", "[OH]-P(=O)(-O)-*");
			//Phosphoric_ester groups
			super.initSingleSMARTS(super.smartsPatterns,"2", "COP(=O)(-*)O");
			super.initSingleSMARTS(super.smartsPatterns,"3", "P(O)O");
			id = "37";
			title = "Heterocycle";
			
			examples[0] = "";
			examples[1] = "";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}

	}

}