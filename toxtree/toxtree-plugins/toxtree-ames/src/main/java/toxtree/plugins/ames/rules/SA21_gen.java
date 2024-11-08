/*
Copyright Ideaconsult Ltd. (C) 2005-2012 

Contact: jeliazkova.nina@gmail.com

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

package toxtree.plugins.ames.rules;
import java.util.logging.Level;

import toxTree.tree.rules.StructureAlert;
import ambit2.smarts.query.SMARTSException;

public class SA21_gen extends StructureAlert {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4846012217142617415L;
	public static String SA21_title = "Alkyl and aryl N-nitroso groups";
	/**
	 * TODO
Please, could you add in this alert also aryl or alkyl substances with the substructure: -N=N-O-H?
Perhaps with the following MSARTS?

[C,c]N[NX2;v3]=O
OR 
[C,c]N=N[OH1]

	 */
    public SA21_gen() {
        super();
        try {
            addSubstructure(SA21_title, "[C,c]N[NX2;v3]=O");
            setID("SA21_Ames");
            setTitle(SA21_title);
            setExplanation(SA21_title);
            
            examples[0] = "CCN(CC)N(=O)O";
            examples[1] = "O=NNCCC";   
            editable = false;
        } catch (SMARTSException x) {
        	logger.log(Level.SEVERE,x.getMessage(),x);
        }
    }	
}


