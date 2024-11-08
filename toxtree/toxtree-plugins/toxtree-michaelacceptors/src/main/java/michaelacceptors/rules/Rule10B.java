package michaelacceptors.rules;
import java.util.logging.Level;

import toxTree.tree.rules.StructureAlertAmbit;
import ambit2.smarts.query.SMARTSException;

public class Rule10B extends StructureAlertAmbit {
	private static final long serialVersionUID = 0;
	public Rule10B() {
		super();		
		try {
						
			id = "10B";
			setTitle("Para-vinyl azaarene");
			addSubstructure(getTitle(),"C=[CH]c1ccncc1");
			examples[0] = "";
			examples[1] = "C=[CH]c1ccncc1";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}

	}

}
