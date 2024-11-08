/*
Copyright Ideaconsult Ltd.(C) 2006 -2008
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
 * 
 * Aliphatic esters of chloro formic acid
 * @author Nina Jeliazkova nina@acad.bg
 */
public class Rule17 extends RuleSMARTSubstructureCDK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7350155371387383600L;
	public static final String MSG17 = "Aliphatic esters of chloro formic acid";

	public Rule17() {
		
		super();		
		try {
			addSubstructure("ClC(=O)([OX2][C])");
			//ClC(=O)([OX2][AR0])");
			setID("17");
			setTitle(MSG17);
			editable = false;
			examples[0] = "CC(OC)=O";
			examples[1] = "ClC(OC)=O";
			
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}
	}
}
