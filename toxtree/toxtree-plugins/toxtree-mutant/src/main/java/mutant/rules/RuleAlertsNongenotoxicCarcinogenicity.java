/*
Copyright (C) 2005-2006  

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

package mutant.rules;

import toxTree.tree.rules.RuleInitAlertCounter;

public class RuleAlertsNongenotoxicCarcinogenicity extends RuleInitAlertCounter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7260662529206464920L;

	public RuleAlertsNongenotoxicCarcinogenicity() {
		super();
		setID("Alerts(nongenotoxic)");
		setTitle("Verify structural alerts for potential nongenotoxic carcinogenicity");
        StringBuffer b = new StringBuffer();
        String[] s = new String[] {
                "SA17_nogen","SA31a_nogen","SA31b_nogen","SA31c_nogen"
        };
        for (int i=0; i < s.length;i++) {
            b.append("<a href=\"#");
            b.append(s[i]);
            b.append("\">");
            b.append(s[i]);
            b.append("</a>");
        }
        setExplanation(b.toString());
	}
}


