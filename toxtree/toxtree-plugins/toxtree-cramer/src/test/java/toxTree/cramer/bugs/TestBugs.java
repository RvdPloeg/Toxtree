package toxTree.cramer.bugs;

import junit.framework.Assert;

import org.junit.Test;

import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionResult;

/**
 * https://sourceforge.net/tracker/?func=detail&aid=3445043&group_id=152702&atid=785126
 * @author nina
 *
 */
public class TestBugs extends AbstractTreeTest {

	@Test
	public void test3445043() throws Exception {
		IDecisionResult result = classify("CC1=CC(N=C2C([N]3)=O)=C(C=C1C)N(CC(C(C(COP([O-])(O)=O)[O])[O])[O])C2=NC3=O.OCC[NH2+]CCO");
		Assert.assertEquals(3,result.getCategory().getID());
		String explanation = cr.explainRules(result,false).toString();
		Assert.assertEquals("1N,2Y",explanation);
	}
	
	@Test
	public void test3493804() throws Exception {
		IDecisionResult result = classify("CC(C)CCCC(C)CCCC(CCCC1(C)Oc2c(c(C)c(O)c(C)c2C)CC1)C");
		Assert.assertEquals(2,result.getCategory().getID());
		
		result = classify("C/C=C/c1ccc(OCC)c(O)c1");
		Assert.assertEquals(2,result.getCategory().getID());
		
		 result = classify("C=CC(N)=O");
		Assert.assertEquals(3,result.getCategory().getID());

	}
	
	@Test
	public void test3492853_phenylalanine() throws Exception {
		//phenylalanine
		IDecisionResult result = classify("O=C(O)C(N)Cc1ccccc1");
		Assert.assertEquals(1,result.getCategory().getID());
		String explanation = cr.explainRules(result,false).toString();
		Assert.assertEquals("1Y",explanation);
		
	}
	/**
	 * http://sourceforge.net/p/toxtree/bugs/62/
	 * @throws Exception
	 */
	@Test
	public void test3492853() throws Exception {
		IDecisionResult	result = classify("N=C(N)NCCCC(N)C(=O)O");
		Assert.assertEquals(1,result.getCategory().getID());
		Assert.assertEquals("1Y",cr.explainRules(result,false).toString());
		
		result = classify("N=C(N)NCCCC(N)C(=O)O");
		Assert.assertEquals(1,result.getCategory().getID());
		Assert.assertEquals("1Y",cr.explainRules(result,false).toString());		
	
		result = classify("O=C1O[C@H]([C@H](CO)O)C(O)=C1O");
		Assert.assertEquals(1,result.getCategory().getID());
		Assert.assertEquals("1Y",cr.explainRules(result,false).toString());	
		
		result = classify("O=C1O[C@H]([C@@H](CO)O)C(O)=C1O");
		Assert.assertEquals(1,result.getCategory().getID());
		Assert.assertEquals("1Y",cr.explainRules(result,false).toString());	
		
		
	}	
	/**
	 * /http://sourceforge.net/p/toxtree/bugs/79/
	 */ 
	@Test
	public void test79() throws Exception {
		//Erucamide 
		IDecisionResult result = classify("O=C(N)CCCCCCCCCCCC=CCCCCCCCC");
		Assert.assertEquals(1,result.getCategory().getID());
		String explanation = cr.explainRules(result,false).toString();
		//Assert.assertEquals("1N,2N,3N,5N,6N,7N,16N,17N,19Y,20N,22N,33N",explanation);
		//Oleamide 
		result = classify("O=C(N)CCCCCCCC=CCCCCCCCC");
		Assert.assertEquals(1,result.getCategory().getID());
		explanation = cr.explainRules(result,false).toString();
		//Assert.assertEquals("1N,2N,3N,5N,6N,7N,16N,17N,19Y,20N,22N,33N",explanation);

		
	}
	//
	@Test
	public void testNitrile() throws Exception {
		//Erucamide 
		IDecisionResult result = classify("c1ccc2c(c1)cc([nH]2)C#N");
		IDecisionCategory c = result.getCategory();
		System.out.println(c);
		String explanation = cr.explainRules(result,false).toString();
		//Assert.assertEquals("1N,2N,3N,5N,6N,7N,16N,17N,19Y,20N,22N,33N",explanation);
		System.out.println(explanation);

		
	}
}
