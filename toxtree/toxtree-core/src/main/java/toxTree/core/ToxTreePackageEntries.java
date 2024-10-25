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
package toxTree.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * a {@link java.util.List}  of  {@link ToxTreePackageEntry}
 * @author Nina Jeliazkova
 *
 */
public class ToxTreePackageEntries extends ArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 654366469816332059L;

	public ToxTreePackageEntries() {
		super();
	}

	public ToxTreePackageEntries(int arg0) {
		super(arg0);
	}

	public ToxTreePackageEntries(Collection arg0) {
		super(arg0);
	}

}
