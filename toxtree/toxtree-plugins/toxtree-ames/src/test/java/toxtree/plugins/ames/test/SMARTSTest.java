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

package toxtree.plugins.ames.test;

import junit.framework.TestCase;

import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.query.MolAnalyser;
import ambit2.smarts.query.SmartsPatternCDK;

public class SMARTSTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    public void test() throws Exception {
        //"[cR1r6]!@[cR1r6]"
        String smarts = "c1ccccc1!@c2ccccc2";
        assertEquals(0,match(smarts,"C=1C=CC=2NC(=CC=2(C=1))C=3C=CC=C(C=3)Cl"));
        assertTrue(match(smarts,"c1ccccc1-c2ccccc2")>0);
    }
    public int match(String smarts, String smiles) throws Exception {
        IAtomContainer mol = toxTree.query.FunctionalGroups.createAtomContainer(smiles);
        MolAnalyser.analyse(mol);
        SmartsPatternCDK sm = new SmartsPatternCDK();
        sm.setSmarts(smarts);
        return sm.match(mol);
    }    
}
