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
package sicret.rules;

import toxTree.tree.rules.RuleVerifyProperty;

/**
 * VapourPressure <  0.0001. Expects property to be read from IMolecule.getProperty({@link #VapourPressure}).
 * @author Nina Jeliazkova nina@acad.bg
 * @author Martin Martinov
 * <b>Modified</b> Dec 17, 2006
 */
public class RuleVapourPressure extends RuleVerifyProperty {
	public static String VapourPressure="Vapour Pressure";
	/**
	 * 
	 */
	private static final long serialVersionUID = 6656157974877531909L;
	public RuleVapourPressure(String propertyName, String units, String condition, double value) {
		super(propertyName,units,condition,value);
		id = "8";
	}	
	
	public RuleVapourPressure()
	{
		this(VapourPressure,"Pa",condition_lower,0.0001);
		id = "8";

		examples[0]= "O=C(OCCOCC)C(=C)C";
		propertyExamples[0] = 0.775103;
		examples[1] = "O=C(O)c1ccsc1NC=O";
		propertyExamples[1] = 0.000001;			
	}


}
