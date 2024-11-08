package structurefeatures.rules;

import java.util.logging.Level;

import toxTree.tree.rules.smarts.RuleSMARTSSubstructureAmbit;
import ambit2.smarts.query.SMARTSException;
public class RuleArylMethylHalide extends RuleSMARTSSubstructureAmbit {
	private static final long serialVersionUID = 0;
	public RuleArylMethylHalide() {
		super();		
		try {
			super.initSingleSMARTS(super.smartsPatterns,"1", "a-[$([CX4,CH,CH2,CH3][Cl]),$([#6][Cl])]");			
			id = "22";
			title = "aryl methyl halide";
			
			examples[0] = "";
			examples[1] = "";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}

	}

}

