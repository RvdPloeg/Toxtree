/*
Copyright (C) 2005-2008  

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation; either version 2.1
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
*/

package eye.categories;

import toxTree.tree.NonToxicCategory;

public class CategoryNotCorrosive2SkinOrIrritating2Eye extends NonToxicCategory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6871881193252662338L;

	public CategoryNotCorrosive2SkinOrIrritating2Eye() {
		super("NOT lesions R34, R35 or R36",6);
		setExplanation("NOT corrosive to skin or irritating to eye (NOT R34, R35 or R36)");

	}
}


