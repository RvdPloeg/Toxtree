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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import net.idea.modbcum.i.exceptions.AmbitException;

import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.core.IDecisionCategories;
import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionRule;
import toxTree.core.IDescriptorBased;
import toxTree.data.CategoryFilter;
import toxTree.exceptions.DMethodNotAssigned;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.DecisionNode;
import toxTree.tree.ProgressStatus;
import toxTree.tree.RuleResult;
import toxTree.tree.TreeResult;
import toxTree.tree.rules.IAlertCounter;
import toxtree.plugins.ames.categories.CategoryMutagenTA100;
import toxtree.plugins.ames.categories.CategoryNoAlertAmes;
import toxtree.plugins.ames.categories.CategoryNonMutagen;
import toxtree.plugins.ames.categories.CategoryPositiveAlertAmes;
import ambit2.base.data.ILiteratureEntry;
import ambit2.base.data.ILiteratureEntry._type;
import ambit2.base.data.Property;
import ambit2.base.data.PropertyAnnotation;
import ambit2.base.data.PropertyAnnotations;

//import toxtree.plugins.ames.categories.CategoryNonMutagen;


public class AmesMutagenicityTreeResult extends TreeResult {
    protected static String SUFFIX = "SUFFIX";

	protected String[] descriptors = {
			"LSTM1","B5STM1","EHOMO","ELUMO","MR2","MR3","MR5","MR6","I(An)","I(NO2)","I(BiBr)","MR","LogP","Idist"
			};
    protected String[] qsar = {
            "w_QSAR6","w_QSAR8","w_QSAR13"
            };
	/**
	 * 
	 */
	private static final long serialVersionUID = -1505391697761215610L;

	public void assignResult(IAtomContainer mol) throws DecisionResultException {
		if (mol == null) return;
		IDecisionCategories c = decisionMethod.getCategories();
		for (int i=0; i < c.size();i++) {
			String result = Answers.toString(Answers.NO);
			if (getAssignedCategories().indexOf(c.get(i))>-1)
				result = Answers.toString(Answers.YES);
			mol.setProperty(
	        		c.get(i).toString(),
	                result);
		}

        String paths = getClass().getName()+"#explanation";
        if (getDecisionMethod().getRules().size() > 1) {
	        mol.setProperty(
	        		paths,
	                explain(false).toString());
	        Hashtable<String,String> b = getExplanation(mol);
	        Enumeration<String> k = b.keys();
	        while (k.hasMoreElements()) {
	        	String key = k.nextElement();
	        	mol.setProperty(key,b.get(key));
	        }
        } else
        	mol.removeProperty(paths);
        firePropertyChangeEvent(ProgressStatus._pRuleResult, null, status);        

	}

	@Override
	public String[] getResultPropertyNames() {
		IDecisionCategories c = decisionMethod.getCategories();
		String[] names = new String[c.size()];
		for (int i=0; i < c.size();i++) 
			names[i] = c.get(i).toString();
		return names;
	}
	
	@Override
	public List<Property> getResultProperties() throws AmbitException {

		if (getDecisionMethod() == null) throw new AmbitException("Unassigned method");
		ILiteratureEntry le = getReference();
		le.setType(_type.Algorithm);
		List<Property> p = new ArrayList<Property>();
		for (IDecisionCategory category : getDecisionMethod().getCategories()) {
			Property property = new Property(category.toString(),le);
			property.setLabel(le.getURL());
			property.setClazz(String.class);
			property.setOrder(p.size()+1);
			property.setEnabled(true);
			property.setNominal(true);
			PropertyAnnotations pa = new PropertyAnnotations();
			property.setAnnotations(pa);
			PropertyAnnotation a = new PropertyAnnotation();
			a.setType("^^"+category.getCategoryType().name());
			a.setPredicate("acceptValue");
			a.setObject(Answers.toString(Answers.YES));
			pa.add(a);
			a = new PropertyAnnotation();
			a.setType("^^"+category.getNegativeCategoryType().name());
			a.setPredicate("acceptValue");
			a.setObject(Answers.toString(Answers.NO));
			pa.add(a);
			p.add(property);
		}
		
		Property property = new Property(String.format("%s#explanation", getDecisionMethod().getTitle()),le); 
		property.setLabel(le.getURL());
		property.setClazz(String.class);
		property.setOrder(p.size()+1);
		property.setEnabled(true);
		property.setNominal(false);
		p.add(property);
		
		return p;
	}
	
	protected ArrayList<IAtomContainer> getAllAssignedMolecules() {
        ArrayList<IAtomContainer> residues = new ArrayList<IAtomContainer>();
        return residues;
        /*
        for (int i=0;i < ruleResults.size();i++) {
            RuleResult r = ((RuleResult)ruleResults.get(i));
            if (r.isSilent()) continue;
            if (r.getMolecule() != null) {
                if (r.getRule() != null) {
                    System.out.println(r.getRule());
                    System.out.println(r.getMolecule().getID());
                    if (residues.indexOf(r.getMolecule()) == -1) {
                        residues.add(r.getMolecule());
                        r.getMolecule().setProperty("SUFFIX", "_("+residues.size()+")");                        
                    }
                }
            }            
        }
        return residues;
        */
    }

	public Hashtable<String,String> getExplanation(IAtomContainer mol) throws DecisionResultException {
		Hashtable<String,String> b = new  Hashtable<String,String>();
        ArrayList<IAtomContainer> residues = getAllAssignedMolecules();
        IAtomContainer originalMol = getOriginalMolecule();
		try {
		    if (status.isEstimated()) {
		    	for (int i=0;i < ruleResults.size();i++) {
		    		RuleResult r = ((RuleResult)ruleResults.get(i));
		    		if (r.isSilent()) continue;

		    		if ((r.getCategory() == null) 
		    			|| (r.getRule() instanceof IAlertCounter)
		    			|| (
		    			(r.getRule() instanceof DecisionNode) && (((DecisionNode)r.getRule()).getRule() instanceof IAlertCounter)
		    			)
		    			) { //not a leaf node or an alert
		    			if (r.isResult())
		    				b.put(r.getRule().getID(),Answers.toString(Answers.YES));
		    			else 
		    				b.put(r.getRule().getID(),Answers.toString(Answers.NO));
		    		}
		    		//Use this to get descriptor values 
                    
		    		if (r.getMolecule() != null) {
                        
                        Object suffix = "";
                        IDescriptorBased rule = null;
                        if (rule instanceof IDescriptorBased) 
                            rule = (IDescriptorBased) r.getRule();
                        else if ((r.getRule() instanceof DecisionNode) &&
                             ((DecisionNode)r.getRule()).getRule() instanceof IDescriptorBased)
                            rule = (IDescriptorBased) ((DecisionNode)r.getRule()).getRule();
                        

                        if (rule != null) {

    		    			for (int d=0; d< descriptors.length;d++) {
    		    				Object n= r.getMolecule().getProperty(descriptors[d]);
    		    				if (n!= null) {
                                    if ((rule != null) && rule.isCalculated(descriptors[d])) { 
                                        
                                        if (originalMol == r.getMolecule())
                                            suffix = "";
                                        else {
                                            suffix = r.getMolecule().getProperty(SUFFIX);                                            
                                            if ((suffix == null) &&
                                                (residues.indexOf(r.getMolecule()) == -1) ) {
                                                residues.add(r.getMolecule());
                                                
                                                r.getMolecule().setProperty(SUFFIX, "_("+residues.size()+")");
                                                suffix = r.getMolecule().getProperty(SUFFIX);
                                            }
                                            if (suffix == null) suffix = "";
                                        }
        		    					mol.setProperty(descriptors[d]+suffix, n);
                             
                                    }
                                }
    		    					
    		    			}
                            suffix = r.getMolecule().getProperty(SUFFIX);
                            if (suffix == null) suffix = "";
                            for (int d=0; d< qsar.length;d++) {
                                Object n= r.getMolecule().getProperty(qsar[d]);
                                if (n!= null)
                                    if ((rule != null) && rule.isCalculated(qsar[d]))  
                                    mol.setProperty(qsar[d]+suffix, n
                                            );
                                    
                            }		
                        }
                        
		    		}
		    	}
		    	
		    } else if (status.isEstimating()) {
		        b.put(ProgressStatus._eResultIsEstimating,"YES");
		        
		    }   else if (status.isError()) {
		        b.put(ProgressStatus._eError,status.getErrMessage());
		    }

            residues.clear();
		//} catch (DecisionMethodException x) {
			//throw new DecisionResultException(x);
		} catch (NullPointerException x) {
			throw new DMethodNotAssigned(ProgressStatus._eMethodNotAssigned);
		}
		return b;
	}
	
	public void addRuleResult(IDecisionRule rule, boolean value, IAtomContainer molecule)
	throws DecisionResultException {
			super.addRuleResult(rule, value,molecule);
			//if (rule instanceof RuleAlertsForGenotoxicCarcinogenicity)
				//setSilent(true);
			
			if (rule instanceof RuleAlertsForAmesMutagenicity)
				setSilent(true);
			
			if (rule instanceof RuleAlertsForNewAmesMutagenicity)
				setSilent(true);
			
			if (rule instanceof VerifyAlertsNewAmes)
				setSilent(true);
			
			if (rule instanceof VerifyAlertsAmes)
				setSilent(true);
			
			else setSilent((rule instanceof DecisionNode) &&	
				(
			
				
		(((DecisionNode)rule).getRule() instanceof RuleAlertsForAmesMutagenicity)||
		(((DecisionNode)rule).getRule() instanceof RuleAlertsForNewAmesMutagenicity)||
		(((DecisionNode)rule).getRule() instanceof VerifyAlertsAmes)||
		(((DecisionNode)rule).getRule() instanceof VerifyAlertsNewAmes)
				)
			);
				
					
	}
	
	@Override
	protected boolean acceptCategory(IDecisionCategory category) {
		
		if (category == null) return false;
		if (category instanceof CategoryPositiveAlertAmes) {
			removeCategory(new CategoryNoAlertAmes());
				return true;
		//} else	if (category instanceof CategoryPositiveAlertNongenotoxic) {
			//removeCategory(new CategoryNoNongenotoxicAlert());
		//	return true;
		//} else if (category instanceof CategoryCarcinogen) {
		//	removeCategory(new CategoryNotCarcinogen());
		} else if (category instanceof CategoryMutagenTA100) {
			removeCategory(new CategoryNonMutagen());			
		//} else if (category instanceof CategoryNotCarcinogen) {
		//	int alert = assignedCategories.indexOf(new CategoryCarcinogen());
		//	if (alert > -1) return false;
		} else if (category instanceof CategoryNonMutagen) {
			int alert = assignedCategories.indexOf(new CategoryMutagenTA100());
			if (alert > -1) return false;
		} 
		
		return true;
	}
	protected void removeCategory(IDecisionCategory category) {
		int remove = assignedCategories.indexOf(category);
		if (remove > -1)
		assignedCategories.remove(remove);

	}
    public List<CategoryFilter> getFilters() {

    	ArrayList<CategoryFilter> l = new ArrayList<CategoryFilter>();
    	IDecisionCategories c = getDecisionMethod().getCategories();
		for (int i=0; i < c.size();i++) 
		try { 
    		l.add(new CategoryFilter(c.get(i).toString(),Answers.toString(Answers.YES)));
    		l.add(new CategoryFilter(c.get(i).toString(),Answers.toString(Answers.NO)));
    	} catch (Exception x) {
    		logger.log(Level.SEVERE,x.getMessage(),x);
    	}
    	return l;
    }
}

/**
 * workaround due to problem with XML serialization with Enums
 * @author Nina Jeliazkova nina@acad.bg
 *
 */
class Answers {
	public static int NO=0;
	public static int YES=1;
	protected Answers() {
		
	}
	public static String toString(int answer) {
		switch (answer) {
		case 0: return "NO";
		case 1: return "YES";
		default: return "";
		}
	}
}