/*
Copyright Ideaconsult Ltd.(C) 2006  
Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/
package eye.rules;


import java.util.logging.Level;

import toxTree.tree.rules.smarts.RuleSMARTSubstructureCDK;
import ambit2.smarts.query.SMARTSException;

/**
 * @author Nina Jeliazkova nina@acad.bg
 */
public class Rule28 extends  RuleSMARTSubstructureCDK{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8607025658661359589L;
	private static final String MSG28="Triphenylphosphonium salts";
	public Rule28() {
		super();		
		try {
			addSubstructure("c1([cH][cH][cH][cH][cH]1)[P+](c2[cH][cH][cH][cH][cH]2)(c3[cH][cH][cH][cH][cH]3)[CH2]");
			setID("28");
			setTitle(MSG28);
			
			editable = false;
			examples[0] = "c1(ccccc1)C(c2ccccc2)(c3ccccc3)CC=C";
			examples[1] = "c1(ccccc1)[P+](c2ccccc2)(c3ccccc3)CC=C";					
			
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}
	}
}

