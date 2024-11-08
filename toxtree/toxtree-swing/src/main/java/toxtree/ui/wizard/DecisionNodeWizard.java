/*
Copyright (C) 2005-2009 

Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.
All we ask is that proper credit is given for our work, which includes
- but is not limited to - adding the above copyright notice to the beginning
of your source code files, and to any copyright notice that you may distribute
with programs based on this work.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package toxtree.ui.wizard;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionCategoryEditor;
import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionRule;
import toxTree.core.IDecisionRuleEditor;
import toxTree.core.Introspection;
import toxTree.core.ToxTreePackageEntries;
import toxTree.core.ToxTreePackageEntry;
import toxTree.tree.DecisionNode;
import toxTree.tree.DecisionNodesList;
import toxTree.tree.DefaultCategory;
import toxTree.tree.rules.RuleAllSubstructures;
import toxTree.tree.rules.RuleAnySubstructure;
import toxTree.tree.rules.RuleAromatic;
import toxTree.tree.rules.RuleDescriptorRange;
import toxTree.tree.rules.RuleManyAromaticRings;
import toxTree.tree.rules.RuleMolecularMassRange;
import toxTree.tree.rules.RuleStructuresList;
import toxTree.tree.rules.RuleVerifyProperty;
import toxTree.tree.rules.smarts.RuleSMARTSubstructure;
import toxtree.ui.tree.ListPanel;
import toxtree.ui.tree.ListTableModel;
import toxtree.ui.tree.ToxTreePackageEntryModel;
import toxtree.ui.tree.actions.NewRuleAction;
import toxtree.ui.tree.rules.DecisionNodesListTableModel;

import com.nexes.wizard.WizardPanelDescriptor;

public class DecisionNodeWizard extends DecisionTreeWizard implements ListSelectionListener {
    public enum RuleOptions {
	options_allsubtructure {
	    @Override
	    public String toString() {
		return "All substructures";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleAllSubstructures();
	    }
	},
	options_anysubtructure {
	    @Override
	    public String toString() {
		return "Any substructure";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleAnySubstructure();
	    }
	},
	options_smarts {
	    @Override
	    public String toString() {
		return "SMARTS";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleSMARTSubstructure();
	    }
	},
	options_structure {
	    @Override
	    public String toString() {
		return "Exact structures (from file)";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleStructuresList();
	    }
	},
	options_aromatic {
	    @Override
	    public String toString() {
		return "Aromatic";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleAromatic();
	    }
	},
	options_heteroaromatic {
	    @Override
	    public String toString() {
		return "Heteroaromatic";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		try {
		    return (IDecisionRule) Introspection.loadCreateObject("toxTree.tree.cramer.RuleHeteroaromatic");
		} catch (Exception x) {
		    return null;
		}
	    }
	},

	options_aromaticrings {
	    @Override
	    public String toString() {
		return "Number of aromatic rings";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleManyAromaticRings();
	    }
	},
	options_descriptorrrange {
	    @Override
	    public String toString() {
		return "Descriptor";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleDescriptorRange();
	    }
	},
	options_property {
	    @Override
	    public String toString() {
		return "Property";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleVerifyProperty();
	    }
	},
	options_molweight {
	    @Override
	    public String toString() {
		return "Molecular weight";
	    }

	    @Override
	    IDecisionRule getRule() throws Exception {
		return new RuleMolecularMassRange();
	    }
	};
	abstract IDecisionRule getRule() throws Exception;
    };

    protected static String[] pages = { "options", "categoryoptions", "ruleoptions", "panel", "details",
	    "introspection" };
    public static int pageOptions = 0;
    public static int pageCategoryOptions = 1;
    public static int pageRuleOptions = 2;
    public static int pagePanel = 3;
    public static int pageDetails = 4;
    public static int pageIntrospection = 5;
    public static int pageAll = 5;
    protected ToxTreeWizardPanelDescriptor[] descriptors;
    protected EditorPanel editorPanel = null;
    protected EditorPanel nodePanel = null;
    protected Object selectedObject = null;
    protected EditorPanel introspectionPanel = null;
    protected ListPanel categoriesListPanel = null;
    protected ListPanel rulesListPanel = null;
    protected int startIndex = pageOptions;

    public synchronized Object getSelectedObject() {
	if (selectedObject instanceof DecisionNode)
	    return selectedObject;
	else if (selectedObject instanceof IDecisionCategory)
	    return selectedObject;
	else if (selectedObject instanceof IDecisionRule) {
	    return createNewNode((IDecisionRule) selectedObject);
	} else
	    return selectedObject;
    }

    public synchronized void setSelectedObject(Object selectedObject) {
	this.selectedObject = selectedObject;
    }

    public DecisionNodeWizard(IDecisionMethod tree, ListPanel[] panels) {
	super(tree);
	addWidgets(panels);
    }

    public DecisionNodeWizard(Dialog arg0, IDecisionMethod tree, ListPanel[] panels) {
	super(arg0, tree);
	addWidgets(panels);
    }

    public DecisionNodeWizard(Frame frame, IDecisionMethod tree, ListPanel[] panels) {
	super(frame, tree);
	addWidgets(panels);
    }

    public static DecisionNodeWizard createWizard(Frame frame, IDecisionMethod tree, int start) {
	ListPanel[] panels = new ListPanel[2];
	if (tree.getRules() instanceof DecisionNodesList)
	    panels[0] = new ListPanel("Rules", new DecisionNodesListTableModel(tree.getRules()), null);
	else
	    panels[0] = new ListPanel("Rules", new ListTableModel(tree.getRules()), null);
	panels[1] = new ListPanel("Categories", new ListTableModel(tree.getCategories()), null);
	DecisionNodeWizard wizard = new DecisionNodeWizard(frame, tree, panels);
	wizard.setStartIndex(start);
	return wizard;
    }

    public void setStartIndex(int start) {
	ToxTreeWizardPanelDescriptor d = getDescriptor(start);
	d.setBackId(null);
	setCurrentPanel(getDescriptor(start).getId().toString());
	startIndex = start;
    }

    protected ToxTreeWizardPanelDescriptor getDescriptor(int index) {
	return descriptors[index];
    }

    protected void addWidgets(ListPanel[] panels) {
	descriptors = new ToxTreeWizardPanelDescriptor[pageAll + 1];
	ArrayList options = new ArrayList();
	for (int i = 0; i < panels.length; i++) {
	    options.add(panels[i]);
	    panels[i].addListSelectionListener(this);
	}
	int selected = 0;
	RadioBoxPanel optionsPanel = new RadioBoxPanel("Select", options, selected) {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = -6894488311524888010L;

	    @Override
	    public void selectObject(ActionEvent e, Object object) {
		if (object instanceof ListPanel) {
		    ListPanel lp = ((ListPanel) object);
		    editorPanel.setVisualComponent(lp);
		    switch (getSelectedIndex()) {
		    case 1: {// category
			descriptors[pageOptions].setNextId(pages[pageCategoryOptions]);
			descriptors[pageCategoryOptions].setBackId(pages[pageOptions]);
			break;
		    } // rule
		    default: {
			descriptors[pageOptions].setNextId(pages[pageRuleOptions]);
			descriptors[pageRuleOptions].setBackId(pages[pageOptions]);
			break;
		    }

		    }
		}
	    };
	};

	descriptors[pageOptions] = new ToxTreeWizardPanelDescriptor(pages[pageOptions], pages[pageRuleOptions], null,
		optionsPanel, null);

	/** category */
	ArrayList categoryOptions = new ArrayList();
	categoryOptions.add("Select from categories used in the tree");
	categoryOptions.add("Select from all available categories");
	categoryOptions.add("Create new category");
	RadioBoxPanel categoryOptionsPanel = new RadioBoxPanel("Select", categoryOptions, 0) {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = -4697858026021861009L;

	    @Override
	    public void selectObject(ActionEvent e, Object object) {
		if (object.equals("Create new category")) {
		    IDecisionCategory c = new DefaultCategory();
		    tree.getCategories().add(c);
		    c.setName(Integer.toString(c.getID()));
		    setSelectedObject(c);
		    nodePanel.setEditor(c.getEditor());
		    descriptors[pageCategoryOptions].setNextId(pages[pageDetails]);
		    descriptors[pageDetails].setBackId(pages[pageCategoryOptions]);
		} else if (object.equals("Select from all available categories")) {
		    if (categoriesListPanel == null) {
			ToxTreePackageEntries categoryTypes = Introspection.getAvailableCategoryTypes(this.getClass()
				.getClassLoader());
			categoriesListPanel = new ListPanel("Categories", new ToxTreePackageEntryModel(categoryTypes),
				null);
			categoriesListPanel.addListSelectionListener(new ListSelectionListener() {
			    public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
				    return;
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				if (!lsm.isSelectionEmpty())
				    try {
					Object o = categoriesListPanel.getValueAt(lsm.getMinSelectionIndex(), 2);
					processSelectedObject(o);
				    } catch (Exception x) {
					x.printStackTrace();
				    }
			    }
			});
		    }
		    introspectionPanel.setVisualComponent(categoriesListPanel);
		    descriptors[pageCategoryOptions].setNextId(pages[pageIntrospection]);
		    descriptors[pageIntrospection].setBackId(pages[pageCategoryOptions]);
		    descriptors[pageDetails].setBackId(pages[pageIntrospection]);

		} else {
		    descriptors[pageCategoryOptions].setNextId(pages[pagePanel]);
		    descriptors[pagePanel].setBackId(pages[pageCategoryOptions]);
		}
	    };
	};

	descriptors[pageCategoryOptions] = new ToxTreeWizardPanelDescriptor(pages[pageCategoryOptions],
		pages[pagePanel], null, categoryOptionsPanel, null);

	/** rule */
	final String _options_treerules = "Select from rules used in the tree";
	final String _options_allrules = "Select from all available rules";
	ArrayList ruleOptions = new ArrayList();
	ruleOptions.add(_options_treerules);
	ruleOptions.add(_options_allrules);
	for (RuleOptions o : RuleOptions.values())
	    ruleOptions.add(o);

	RadioBoxPanel ruleOptionsPanel = new RadioBoxPanel("Select", ruleOptions, 0) {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = 2843576954397333305L;

	    @Override
	    public void selectObject(ActionEvent e, Object object) {

		try {
		    RuleOptions option = (RuleOptions) object;
		    IDecisionRule c = option.getRule();
		    setSelectedObject(c);
		    nodePanel.setEditor(c.getEditor());
		    descriptors[pageRuleOptions].setNextId(pages[pageDetails]);
		    descriptors[pageDetails].setBackId(pages[pageRuleOptions]);
		} catch (Exception x) {
		    if (object.equals(_options_allrules)) {
			if (rulesListPanel == null) {
			    ToxTreePackageEntries ruleTypes = Introspection.getAvailableRuleTypes(this.getClass()
				    .getClassLoader());
			    rulesListPanel = new ListPanel("Rules", new ToxTreePackageEntryModel(ruleTypes), null);
			    rulesListPanel.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
				    if (e.getValueIsAdjusting())
					return;
				    ListSelectionModel lsm = (ListSelectionModel) e.getSource();

				    if (!lsm.isSelectionEmpty())
					try {
					    Object o = rulesListPanel.getValueAt(lsm.getMinSelectionIndex(), 2);
					    processSelectedObject(o);
					} catch (Exception x) {
					    x.printStackTrace();
					}
				}
			    });
			}
			introspectionPanel.setVisualComponent(rulesListPanel);
			descriptors[pageRuleOptions].setNextId(pages[pageIntrospection]);
			descriptors[pageIntrospection].setBackId(pages[pageRuleOptions]);
			descriptors[pageDetails].setBackId(pages[pageIntrospection]);
			/*
			 * Object name =
			 * selectFromList(parent,"Select a rule","Available rules:"
			 * , new ToxTreePackageEntryModel(ruleTypes), null); if
			 * ((name != null) && (name instanceof
			 * ToxTreePackageEntry)) { Object o =
			 * Introspection.loadCreateObject(((ToxTreePackageEntry)
			 * name).getClassName()); if (o instanceof
			 * IDecisionRule) return new
			 * DecisionNode((IDecisionRule) o); else { o = null;
			 * return null; } } else return null;
			 * 
			 * }
			 */

		    } else {
			descriptors[pageRuleOptions].setNextId(pages[pagePanel]);
			descriptors[pagePanel].setBackId(pages[pageRuleOptions]);
		    }
		}
	    };
	};

	descriptors[pageRuleOptions] = new ToxTreeWizardPanelDescriptor(pages[pageRuleOptions], pages[pagePanel],
		pages[pageOptions], ruleOptionsPanel, null) {
	    @Override
	    public void aboutToDisplayPanel() {
		super.aboutToDisplayPanel();
		setBackButtonEnabled(startIndex == pageOptions);
	    }
	};

	editorPanel = new EditorPanel();
	editorPanel.setVisualComponent(panels[selected]);
	descriptors[pagePanel] = new ToxTreeWizardPanelDescriptor(pages[pagePanel], pages[pageDetails],
		pages[pageOptions], editorPanel, null) {
	    @Override
	    public void aboutToDisplayPanel() {
		super.aboutToDisplayPanel();
		setNextFinishButtonEnabled(false);
	    }
	};

	nodePanel = new EditorPanel();
	descriptors[pageDetails] = new ToxTreeWizardPanelDescriptor(pages[pageDetails], WizardPanelDescriptor.FINISH,
		pages[pagePanel], nodePanel, null);

	ToxTreePackageEntries ruleTypes = Introspection.getAvailableRuleTypes(this.getClass().getClassLoader());
	introspectionPanel = new EditorPanel();

	descriptors[pageIntrospection] = new ToxTreeWizardPanelDescriptor(pages[pageIntrospection], pages[pageDetails],
		pages[pageOptions], introspectionPanel, null) {
	    @Override
	    public void aboutToDisplayPanel() {
		super.aboutToDisplayPanel();
		setNextFinishButtonEnabled(false);
	    }
	};

	for (int i = 0; i <= pageAll; i++)
	    registerWizardPanel(descriptors[i].getId().toString(), descriptors[i]);

	setStartIndex(pageOptions);
	// setCurrentPanel(descriptors[pageOptions].getId().toString());
	// setCurrentPanel(descriptors[pageRuleOptions].getId().toString());

    }

    private DecisionNode createNewNode(IDecisionRule rule) {
	DecisionNode node = NewRuleAction.updateNode(tree, new DecisionNode(rule));
	node.setID(Integer.toString(tree.getRules().size()));
	node.setEditable(true);
	tree.getRules().add(node);
	setSelectedObject(node);
	nodePanel.setEditor(node.getEditor());
	return node;
    }

    public void valueChanged(ListSelectionEvent e) {
	if (e.getValueIsAdjusting())
	    return;
	ListSelectionModel lsm = (ListSelectionModel) e.getSource();

	if (!lsm.isSelectionEmpty())
	    try {
		Component c = editorPanel.getVisualComponent();
		if (c instanceof ListPanel) {
		    Object o = ((ListPanel) c).getValueAt(lsm.getMinSelectionIndex(), 0);
		    processSelectedObject(o);
		}
	    } catch (Exception x) {
		x.printStackTrace();
	    }
    }

    public void processSelectedObject(Object o) {
	if (o instanceof ToxTreePackageEntry)
	    try {
		o = Introspection.loadCreateObject(((ToxTreePackageEntry) o).getClassName());
		if (o instanceof DecisionNode) {
		    setNextFinishButtonEnabled(false);
		    return;
		}
	    } catch (Exception x) {
		x.printStackTrace();
	    }

	if (o instanceof DecisionNode)
	    o = ((DecisionNode) o).getRule();
	setSelectedObject(o);

	if (o instanceof IDecisionRule) {
	    ((IDecisionRule) o).setEditable(true);
	    IDecisionRuleEditor editor = ((IDecisionRule) o).getEditor();
	    editor.setRule((IDecisionRule) o);
	    nodePanel.setEditor(editor);
	    setNextFinishButtonEnabled(true);
	} else if (o instanceof IDecisionCategory) {
	    setNextFinishButtonEnabled(true);
	    IDecisionCategoryEditor editor = ((IDecisionCategory) o).getEditor();
	    // editor.setEditable(true);
	    editor.setCategory((IDecisionCategory) o);
	    nodePanel.setEditor(editor);
	} else {
	    setNextFinishButtonEnabled(false);
	}
    }
}
