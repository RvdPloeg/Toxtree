
package toxtree.plugins.kroes;

import java.util.Observable;

import org.openscience.cdk.qsar.DescriptorSpecification;

import toxTree.tree.CategoriesList;
import toxTree.tree.DecisionNodesList;
import toxTree.tree.UserDefinedTree;

public class Kroes1Tree extends UserDefinedTree
{

    public Kroes1Tree()
    {
        super(new CategoriesList(c_categories), null);
        rules = new DecisionNodesList(categories, c_rules, c_transitions);
        if(rules instanceof Observable)
            ((Observable)rules).addObserver(this);
        setTitle("Kroes TTC decision tree");
        setChanged();
        notifyObservers();

        setPriority(3);
    }    
    
    private static final long serialVersionUID = 0x4b97b63a593def3bL;
    public static final transient String c_rules[] = {
    	toxtree.plugins.kroes.rules.RuleKroesFig1Q1.class.getName(),   //1
    	mutant.rules.RuleAlertsForGenotoxicCarcinogenicity.class.getName(), //2
    	
    	toxtree.plugins.kroes.rules.KroesRule3.class.getName(), //3
        toxtree.plugins.kroes.rules.KroesRule4.class.getName(), //4
        toxtree.plugins.kroes.rules.KroesRule5.class.getName(),  //5
        toxtree.plugins.kroes.rules.KroesRule6.class.getName(),  //6
        toxtree.plugins.kroes.rules.KroesRule7.class.getName(),  //7
        toxtree.plugins.kroes.rules.KroesRule8.class.getName(),  //8
        toxtree.plugins.kroes.rules.KroesRule9.class.getName(),  //9
        toxtree.plugins.kroes.rules.KroesRule10.class.getName(), //10
        toxtree.plugins.kroes.rules.KroesRule11.class.getName(), //11
        toxtree.plugins.kroes.rules.KroesRule12.class.getName(), //12
        
        mutant.rules.SA1_gen.class.getName(), //13
        mutant.rules.SA2_gen.class.getName(), //14
        mutant.rules.SA3_gen.class.getName(), //15
        mutant.rules.SA4_gen.class.getName(), //16
        mutant.rules.SA5_gen.class.getName(), //17
        mutant.rules.SA6_gen.class.getName(), //18
        mutant.rules.SA7_gen.class.getName(), //19
        mutant.rules.SA8_gen.class.getName(), //20
        mutant.rules.SA9_gen.class.getName(), //21
        mutant.rules.SA10_gen.class.getName(), //22
        mutant.rules.SA11_gen.class.getName(), //23
        mutant.rules.SA12_gen.class.getName(), //24
        mutant.rules.SA13_gen.class.getName(), //25
        mutant.rules.SA14_gen.class.getName(), //26
        mutant.rules.SA15_gen.class.getName(), //27
        mutant.rules.SA16_gen.class.getName(), //28
        mutant.rules.SA18_gen.class.getName(), //29
        mutant.rules.SA19_gen.class.getName(),  //30
        mutant.rules.SA21_gen.class.getName(), //31
        mutant.rules.SA22_gen.class.getName(), //32
        mutant.rules.SA23_gen.class.getName(), //33
        mutant.rules.SA24_gen.class.getName(), //34
        mutant.rules.SA25_gen.class.getName(), //35
        mutant.rules.SA26_gen.class.getName(), //36
        mutant.rules.SA27_gen.class.getName(), //37
        mutant.rules.SA28_gen.class.getName(), //38
        mutant.rules.SA28bis_gen.class.getName(), //39
        mutant.rules.SA28ter_gen.class.getName(), //40
        mutant.rules.SA29_gen.class.getName(), //41
        mutant.rules.SA30_gen.class.getName(), //42
        mutant.rules.SA37_gen.class.getName(), //43
        mutant.rules.SA38_gen.class.getName(), //44
        mutant.rules.SA39_gen_and_nogen.class.getName(), //45
        toxtree.plugins.kroes.rules.KroesRule2.class.getName() //46
  
    };
    private static final transient int c_transitions[][] = {
        {2, 0, 0, 3}, //q1
      
        {13, 13, 0, 0}, //q2
        {4, 0, 0, 3}, //q3
        {0, 0, 2, 3}, //q4
        {0, 6, 1, 0}, //q5
        {8, 7, 0, 0}, //q6
        {0, 0, 1, 3}, //q7
        {10, 9, 0, 0}, //q8
        {0, 0, 1, 3}, //q9
        {12, 11, 0, 0}, //q10
        {0, 0, 1, 3}, //q11
        {0, 0, 1, 3}, //q12
        
        {14,14,0,0}, //q13
        {15,15,0,0}, //q14
        {16,16,0,0}, //q15
        {17,17,0,0}, //q16
        {18,18,0,0}, //q17
        {19,19,0,0}, //q18
        
        {20,20,0,0}, //q19
        {21,21,0,0}, //q20
        {22,22,0,0}, //q21
        {23,23,0,0}, //q22
        {24,24,0,0}, //q23
        {25,25,0,0}, //q24
        {26,26,0,0}, //q25
        {27,27,0,0}, //q26
        {28,28,0,0}, //q27
        {29,29,0,0}, //q28
        {30,30,0,0}, //q29

        {31,31,0,0}, //q30
        {32,32,0,0}, //q31
        {33,33,0,0}, //q32
        {34,34,0,0}, //q33
        {35,35,0,0}, //q34
        {36,36,0,0}, //q35
        {37,37,0,0}, //q36
        {38,38,0,0}, //q37
        {39,39,0,0}, //q38
        {40,40,0,0}, //q39
        
        {41,41,0,0}, //q40
        {42,42,0,0}, //q41
        
        {43,43,0,0}, //q42
        {44,44,0,0}, //q43
        {45,45,0,0}, //q44
        {46,46,0,0}, //q45
        
        {5,3,0,0}, //q46
        
    };
    private static final transient String c_categories[] = {
    	"toxtree.plugins.kroes.categories.NotASafetyConcern", //1
        "toxtree.plugins.kroes.categories.NegligibleRisk", //2
        "toxtree.plugins.kroes.categories.RequireCompoundSpecificToxicityData",  //3
    };
	public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "http://toxtree.sourceforge.net/kroes.html",
                getTitle(),
                this.getClass().getName(),                
                "Toxtree plugin");
	}
	


}
