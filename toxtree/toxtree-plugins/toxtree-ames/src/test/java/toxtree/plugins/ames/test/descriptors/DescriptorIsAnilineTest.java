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

package toxtree.plugins.ames.test.descriptors;

import org.openscience.cdk.qsar.IMolecularDescriptor;

import toxtree.plugins.ames.descriptors.DescriptorIsAniline;
import ambit2.core.smiles.SmilesParserWrapper;

public class DescriptorIsAnilineTest extends DescriptorsTest {

    protected void setUp() throws Exception {
        super.setUp();
        addPropertiesToTest("I(Aniline)","I(An)");

        SmilesParserWrapper.getInstance().setParser(SmilesParserWrapper.SMILES_PARSER.OPENBABEL);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected IMolecularDescriptor createDescriptorToTest() throws Exception {
        return new DescriptorIsAniline();
    }

    @Override
    public String getResultsFile() {
        return "aromatic_amines/qsar8train_aniline.csv";      
    }

    @Override
    public String getSourceFile() {
        return "aromatic_amines/qsar8train.csv";

    }

    @Override
    public String getStructureID() {
        return "CAS Number";
    }
    /*
    public void testCalculate() throws Exception {
        DescriptorIsAniline d = new DescriptorIsAniline();
        IAtomContainer ac = FunctionalGroups.createAtomContainer("CCCCCc1ccccc1(N)");
        MolAnalyser.analyse(ac);
        IAtomContainer ac1 = FunctionalGroups.createAtomContainer("CCCCCc1ccccc1(NCCC)");
        MolAnalyser.analyse(ac);
        try {
            assertTrue(((BooleanResult) ((DescriptorValue) d.calculate(ac)).getValue()).booleanValue());
            assertFalse(((BooleanResult) ((DescriptorValue) d.calculate(ac1)).getValue()).booleanValue());
        } catch (CDKException x) {
            fail(x.getMessage());
        }
    }
    */
	public void testAniline() throws Exception {
		 
        Object[][] smiles = new Object[][] {
        		
         //{"CCCCCc1ccccc1(N)","I(An)",new Boolean(true)},
//         {"CCCCCc1ccccc1(NCCC)","I(An)",new Boolean(true)},
        		
        //{"CC(=O)NC1=C(C)C=CC=C1(C)","I(An)",new Boolean(true)},
        		
       //{"O=C(C)NC1=C(C)C=CC=C1(C)","I(An)",new Boolean(true)},
      {"CC=2C=CC=C(C)C=2(NC(=O)CN1CCCC1(=O))","I(An)",new Boolean(true)},
       // {"c1(C)cccc(C)c1(NC(=O)CN1CCCC1(=O))","I(An)",new Boolean(true)},        		
         
         

         };
        calculate(smiles);
	}     
	public void testNitro() throws Exception {
		 
        Object[][] smiles = new Object[][] {
        		
         {"c1ccccc1[N+](=O)([O-])","I(An)",new Boolean(false)},
         };
        calculate(smiles);
	}  	
    //CC=2C=CC=C(C)C=2(NC(=O)CN1CCCC1(=O))

}
