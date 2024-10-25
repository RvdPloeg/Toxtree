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

package eye.rules;

import sicret.rules.RuleLogP;

public class RuleLogP_11_6 extends RuleLogP {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5363867593294848441L;

	public RuleLogP_11_6()
	{
		super(LogKow,"",condition_higher,1.5);
		id = "11.6";
		examples[0] = "N12[C@@H]([C@@H](NC(Cc3cccs3)=O)C2=O)SCC(=C1C(=O)[O-])C[n+]1ccccc1";
		examples[1] = "c12N(c3c(cccc3)Sc1ccc(c2)[S@@](C)=O)CC[C@@H]1[N@@](CCCC1)C";		
	}

}


