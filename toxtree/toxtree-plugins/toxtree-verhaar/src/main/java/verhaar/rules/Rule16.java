/*
Copyright Nina Jeliazkova (C) 2005-2011  
Contact: jeliazkova.nina@gmail.com

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
package verhaar.rules;

import verhaar.rules.helper.RuleElementsCounter;



/**
 * 
 * Contain only C,H,N.
 * @author Nina Jeliazkova jeliazkova.nina@gmail.com
 * <b>Modified</b> July 10, 2011
 */
public class Rule16 extends RuleElementsCounter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8079213460512460167L;

	public Rule16() {
		super();
		id = "1.6";
		setComparisonMode(modeAllSpecifiedElements);
		addElement("C");
		addElement("H");
		addElement("N");
		setTitle("Contain only C,H,N");		
		examples[0] = "CCCCO";
		examples[1] = "CCCN(CCC)(CC)";
		setExplanation("Contain only C,H and N");
		editable = false;
	}

}
