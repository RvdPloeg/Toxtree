/*
Copyright Ideaconsult Ltd. (C) 2005-2007 

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/
package toxTree.query;

import org.openscience.cdk.Association;
import org.openscience.cdk.interfaces.IAtom;

/**
 * 
 * <b>Modified</b> 2005-9-23
 * <b>Modified</b> 2012-2-25
 */
public class MyAssociationBond extends Association {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8560854906118304413L;

	/**
	 * 
	 */
	public MyAssociationBond() {
		super();
	}

	/**
	 * @param atom1
	 * @param atom2
	 */
	public MyAssociationBond(IAtom atom1, IAtom atom2) {
		super(atom1, atom2);
	}


}