/*
Copyright Ideaconsult Ltd. (C) 2005-2007  

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

package toxtree.plugins.func.rules;

import java.util.logging.Level;

import ambit2.smarts.query.SMARTSException;

public class FG20 extends FG {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3847918414478370174L;

	public FG20() {
		try {
			setID("FG20");
            setTitle("enamine");
           
          addSubstructure("FG20", "[$([CX3]([#6])[#6]),$([CX3H][#6]),$([CX3H2]),$([CX3](C=O)C=O),$([CX3H]C=O),$([CX3]([#6])C=O)]=[CX3;$(C(C=O)),$([C][#6]),$([CX3H1])]([$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2]),$([NX3](C=O)C=O),$([NX3H]C=O),$([NX3]([#6])C=O)])");
		} catch (SMARTSException x) {
			logger.log(Level.SEVERE,x.getMessage(),x);
		}	
	}

}


