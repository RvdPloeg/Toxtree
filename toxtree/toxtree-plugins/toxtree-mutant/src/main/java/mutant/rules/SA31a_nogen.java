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

package mutant.rules;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.rules.StructureAlertCDK;
import ambit2.core.data.MoleculeTools;
import ambit2.smarts.query.SMARTSException;

/**
 * TODO Verify how many level of recursive smarts are supported
 * TODO verify what's wrong with matching c;R
 * 
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Jan 5, 2008
 */
public class SA31a_nogen extends StructureAlertCDK {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2036282670921013258L;
	public static String SA31a_title = "Halogenated benzene  (Nongenotoxic carcinogens)";
    public static int index_singlering = 12;
    public static int index_nitro_aromatic = 0;
    public static int index_primary_aromatic_amine = 1;
    public static int index_hydroxyl_amine = 2;
    public static int index_hydroxyl_amineester = 3;
    public static int index_monodialkylamine1 = 4;
    public static int index_monodialkylamine2 = 5;
    public static int index_monodialkylamine3 = 6;    
    public static int index_nacymamine = 7;
    public static int index_diazo = 8;
    public static int index_biphenyl = 9;
    public static int index_diphenyl1 = 10;
    public static int index_diphenyl2 = 11;
	
	/**
Overlaps are accepted, except with the following alerts: 
NA_27, NA_28s (NA_28, NA_28bis, NA_28ter) and NA_29. 
This alert contains the exclusion of diphenyls and biphenyls.
This imply that if in the same molecule there is a diphenyl or a biphenyl and a halogenated benzene, the latter will not be detected. 

	 *
	 */
    public static String hydroxyl = "3 or more hydroxyl groups";
	public static String[][] exclusion_rules_Hal = {
            //{title, smarts, example}
            {"Structures with 2 halogens ortho","[Cl,Br,I,F]cc[Cl,Br,I,F]","FC1=CC=CC=C1Cl"},
            {"Structures with 2 halogens meta","[Cl,Br,I,F]ccc[Cl,Br,I,F]","C=1C=C(C=C(C=1)Br)Cl"},
            {hydroxyl,"[Cl,Br,I,F]c1c([OX2H])c([OX2H])c([OX2H])cc1",""},
            {hydroxyl,"[Cl,Br,I,F]c1c([OX2H])c([OX2H])cc([OX2H])c1",""},
            {hydroxyl,"[Cl,Br,I,F]c1c([OX2H])c([OX2H])ccc1([OX2H])",""},
            {hydroxyl,"[Cl,Br,I,F]c1c([OX2H])cc([OX2H])c([OX2H])c1",""},
            {hydroxyl,"[Cl,Br,I,F]c1c([OX2H])cc([OX2H])cc1([OX2H])",""},
            {hydroxyl,"[Cl,Br,I,F]c1cc([OX2H])c([OX2H])c([OX2H])c1",""}            
    };
    public static Object[][] exclusion_rules = {
        //{title, smarts, example,rulename, result}

        {"Nitro aromatic","c[N+](=O)[O-]","O=[N+]([O-])C=1C=CC=C(C=1)Cl","SA27_gen",new Boolean(false)},
        {"Primary aromatic amine","c[N]([#1,C])([#1,C])","CNC=1C([H])=C([H])C([H])=C(Cl)C=1([H])","SA28_gen",new Boolean(false)},
        {"Hydroxyl amine","cN([OX2H])([#1,C])","[H]ONC1=CC=CC=C1Cl","SA28_gen",new Boolean(false)},
        {"Hydroxyl amine ester","cN([#1,C])OC=O","CCCN(OC=O)C1=CC=CC=C1(Cl)","SA28_gen",new Boolean(false)},
        {"Aromatic mono- and dialkylamine","c[NX3v3]([#1,CH3])([#1,CH3])","CNC=1C=CC=C(Cl)C=1","SA28bis_gen",new Boolean(false)},        
        {"Aromatic mono- and dialkylamine","c[NX3v3]([#1,CH3])([CH2][CH3])","CCN(C)C=1C=CC=C(C=1)Cl","",new Boolean(false)},
        {"Aromatic mono- and dialkylamine","c[NX3v3]([CH2][CH3])([CH2][CH3])","CCN(CC)C=1C=CC=C(Cl)C=1","",new Boolean(false)},
        {"Aromatic N-acyl amine","cNC(=O)[#1,CH3]","CC(=O)NC=1C=CC=C(C=1)Cl","SA28ter_gen",new Boolean(false)},
        {"Aromatic diazo","cN=[N]a","C1=CC=C(C=C1)N=NC=2C=CC=C(C=2)Cl","SA29_gen",new Boolean(false)},
        {"Biphenyls","c!@[cR1r6]1ccccc1","C1=CC=C(C=C1)C2=CC=CC=C2Cl","",new Boolean(false)},
        {"Diphenyls","c!@*!@c1ccccc1","c1c(Cl)c(ccc1Cc2ccc(cc2))","",new Boolean(false)},
   //     {"Diphenyls","c!@*!@*!@c1ccccc1","c1c(Cl)c(ccc1CCc2ccc(cc2))","",new Boolean(false)},
        {"Not in fused rings","[R2]","C=1C=CC=2C(C=1)=CC=CC=2Cl","",new Boolean(false)}
        
    };
    //#1,CH3,$([CH2][CH3])
    /**
The generated string is: 
[c;$([R1]);!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)]1[c;$([R1]);!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c;$([R1]);!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c;$([R1]);!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c;$([R1]);!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c]1([Cl,Br,F,I;!$([Cl,Br,I,F]cc[Cl,Br,I,F]);!$([Cl,Br,I,F]ccc[Cl,Br,I,F])])
     */
    public SA31a_nogen() throws SMARTSException {
        super();
        	setContainsAllSubstructures(true);
        	
            StringBuffer b = new StringBuffer();
            for (int i=0; i < 5; i++) {
                b.append("[");
                b.append("c");
                if (i<5)
                    for (int j=0; j < exclusion_rules.length;j++) {
                        b.append(";");
                        if (!((Boolean)exclusion_rules[j][4]).booleanValue())
                            b.append("!");
                        b.append("$(");      
                        b.append(exclusion_rules[j][1]);
                        b.append(")");
                    }                
                b.append("]");
                if (i==0) b.append("1");
            }
            b.append("c1");
            b.append("(");
            b.append("[");
            b.append("Cl,Br,F,I");
            for (int i=0; i < exclusion_rules_Hal.length;i++) {
                b.append(";");
                b.append("!$(");      
                b.append(exclusion_rules_Hal[i][1]);
                b.append(")");
            }
      
            b.append("]");
            b.append(")");
            
        	addSubstructure(SA31a_title,b.toString());
            

/*
[c;!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)]1[c;!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c;!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c;!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c;!$(c[N+](=O)[O-]);!$(c[N]([#1,C])([#1,C]));!$(cN([OX2H])([#1,C]));!$(cN([#1,C])OC=O);!$(c[NX3v3]([#1,CH3])([#1,CH3]));!$(c[NX3v3]([#1,CH3])([CH2][CH3]));!$(c[NX3v3]([CH2][CH3])([CH2][CH3]));!$(cNC(=O)[#1,CH3]);!$(cN=[N]a);!$(c!@c1ccccc1);!$(c!@*!@c1ccccc1);!$(c!@*!@*!@c1ccccc1)][c]1([Cl,Br,F,I;!$([Cl,Br,I,F]cc[Cl,Br,I,F]);!$([Cl,Br,I,F]ccc[Cl,Br,I,F])])
 */

            setID("SA31a_nogen");
            setTitle(SA31a_title);

            StringBuffer e = new StringBuffer();
            e.append("<html>");
            e.append(SA31a_title);
            e.append("The rule applies only to halogenated benezenes (not naphtalenes, etc.), but it should allow for the presence of other rings in the same molecule.");
            e.append("<br>");
            e.append("However, the following structures should be excluded:");
            e.append("<ul>");
            for (int i=0; i < exclusion_rules_Hal.length;i++) {
                e.append("<li>");
                e.append(exclusion_rules_Hal[i][0]);                
            }
            e.append("<li>");
            e.append("with 3 or more hydroxyl groups.");
            
            Object old = "";
            for (int i=0; i < exclusion_rules.length;i++) {
                if (old.equals(exclusion_rules[i][0])) continue;
                e.append("<li>");
                e.append(exclusion_rules[i][0]);
                if (!"".equals(exclusion_rules[i][3])) {
                    e.append("<a href=\"#");
                    e.append(exclusion_rules[i][3]);
                    e.append("\">");
                    e.append(" (");
                    e.append(exclusion_rules[i][3]);
                    e.append(")");
                    e.append("</a>");
                }
                old = exclusion_rules[i][0];
                                
            }            
            e.append("</ul>");
            e.append("</html>");
            
            setExplanation(e.toString());
            
            examples[0] = "CC(C)OC(=O)NC=1C=C(O)C(O)=C(C=1(O))Cl"; //"OC=1C=CC(O)=C(O)C=1";
            examples[1] = "c1ccccc1Cl";//"NS(=O)(=O)C1=CC(C(=O)O)=C(C=C1Cl)NCC2=CC=CO2";   
            editable = false;
    }		
	protected boolean isAPossibleHit(IAtomContainer mol, IAtomContainer processedObject) throws DecisionMethodException  {
		IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(mol);
		return 
		MolecularFormulaManipulator.containsElement(formula,MoleculeTools.newElement(formula.getBuilder(),"Cl")) ||
		MolecularFormulaManipulator.containsElement(formula,MoleculeTools.newElement(formula.getBuilder(),"F")) ||
		MolecularFormulaManipulator.containsElement(formula,MoleculeTools.newElement(formula.getBuilder(),"I")) ||
		MolecularFormulaManipulator.containsElement(formula,MoleculeTools.newElement(formula.getBuilder(),"Br"));
		
	}    
}


