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

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;

import verhaar.query.FunctionalGroups;

/**
 * 
 * Pyridines with one or two chlorine substituents and/or alkyl substituents.
 * 
 * @author Nina Jeliazkova jeliazkova.nina@gmail.com <b>Modified</b> July 12,
 *         2011
 */
// TODO exclude aryl substituents
public class Rule25 extends Rule21 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3463495375143364381L;

	public Rule25() throws Exception {
		super();
		id = "2.5";
		setTitle("Be pyridines with one or two chlorine substituents and/or alkyl substituents");
		setExplanation("Be pyridines with one or two chlorine substituents and/or alkyl substituents");
		examples[1] = "n1cc(c(cc1CC)Cl)Cl";
		examples[0] = "Clc1ccc(cc1(Cl))CC";
		editable = false;

		setMaxHalogens(2);
		setMaxNitroGroups(0);
	}

	@Override
	protected String[] getHalogens() {
		return new String[] { "Cl" };
	}

	protected QueryAtomContainer createMainStructure() throws CDKException {
		return FunctionalGroups.pyridine_character();
	}

}
