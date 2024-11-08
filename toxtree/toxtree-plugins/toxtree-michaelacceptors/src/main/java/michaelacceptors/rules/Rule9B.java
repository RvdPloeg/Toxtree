package michaelacceptors.rules;
import java.util.logging.Level;

import toxTree.tree.rules.StructureAlertAmbit;
import ambit2.smarts.query.SMARTSException;

public class Rule9B extends StructureAlertAmbit {
	private static final long serialVersionUID = 0;
	public Rule9B() {
		super();		
		try {
						
			id = "9B";
			setTitle("Ortho-vinyl azaarene");
			//addSubstructure(getTitle(), "C=[CH]c1ncccc1");
			addSubstructure(getTitle(), "C=Cc1ncccc1");
			
			examples[0] = "";
			examples[1] = "c1cccnc1C=C";	
			editable = false;		
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}

	}

}