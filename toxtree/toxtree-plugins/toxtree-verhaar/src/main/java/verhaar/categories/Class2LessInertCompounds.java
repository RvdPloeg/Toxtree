/*
Copyright Ideaconsult Ltd (C) 2005-2013 
Contact: Ideaconsult Ltd.

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
package verhaar.categories;


import toxTree.tree.ToxicCategory;

/**
 * 
 * Class 2 (less inert compounds).
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Dec 17, 2006
 */
public class Class2LessInertCompounds extends ToxicCategory {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6642389618393648862L;
	public Class2LessInertCompounds() {
		super("Class 2 (less inert compounds)",2);
		setExplanation("<html><h3>Less inert chemicals:</h3> chemicals that are not reactive, but are slightly more toxic than baseline toxicity due to hydrogen bond donor acidity</html>");
		setThreshold("");
	}
}
