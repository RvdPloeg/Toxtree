/*
Copyright Ideaconsult Ltd. (C) 2005-2013 

Contact: www.ideaconsult.net

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

package toxTree.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;

import net.idea.modbcum.i.exceptions.AmbitException;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;
import org.openscience.cdk.renderer.selection.MultiSelection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import toxTree.core.IDecisionCategories;
import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionMethodEditor;
import toxTree.core.IDecisionResult;
import toxTree.core.IDecisionRule;
import toxTree.core.IDecisionRuleList;
import toxTree.core.IProcessRule;
import toxTree.core.XMLSerializable;
import toxTree.exceptions.DecisionMethodException;
import toxTree.exceptions.DecisionResultException;
import toxTree.exceptions.MolAnalyseException;
import toxTree.exceptions.XMLDecisionMethodException;
import toxTree.query.MolAnalyser;
import toxTree.ui.EditorFactory;
import toxTree.ui.tree.categories.CategoriesRenderer;
import ambit2.rendering.CompoundImageTools;
import ambit2.rendering.IAtomContainerHighlights;

/**
 * An astract class, implementing {@link toxTree.core.IDecisionMethod}
 * interface. Used as a base class for the decision trees in
 * {@link toxTree.apps.ToxTreeApp}. The default editor is
 * {@link TreeEditorPanel}.
 * 
 * @author Nina Jeliazkova <br>
 *         <b>Modified</b> 2005-4-30
 */
public abstract class AbstractTree extends Observable implements
		IDecisionMethod, Observer, XMLSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3786430005546416893L;
	/**
	 * 
	 */

	protected static transient Logger logger = Logger
			.getLogger(AbstractTree.class.getName());
	protected Dimension imageSize = new Dimension(150, 150);
	protected boolean web = false;

	public Dimension getImageSize() {
		return imageSize;
	}

	public void setImageSize(Dimension imageSize) {
		this.imageSize = imageSize;
	}

	protected transient PropertyChangeSupport changes = null;
	protected IDecisionRuleList rules;

	protected IDecisionCategories categories = null;
	protected boolean falseIfRuleNotImplemented = true;

	protected int treeRoot = 1;
	protected String name = "";
	protected String explanation = "";
	protected transient boolean modified = false;
	protected int priority = Integer.MAX_VALUE;

	// protected transient IDecisionMethodEditor editor = null;
	/**
	 * Constructor
	 * 
	 */
	public AbstractTree() {
		this(new CategoriesList(), new RulesList());
	}

	public AbstractTree(IDecisionCategories categories) {
		this(categories, new RulesList());
	}

	public AbstractTree(IDecisionCategories categories, IDecisionRuleList rules) {
		super();
		setRules(rules);
		setCategories(categories);
		setTitle(getClass().getName());
		setExplanation(getTitle());
		setChanged();
		notifyObservers();
	}

	/**
	 * 
	 * 
	 public AbstractTree( IDecisionCategories classes, String[] customRules,
	 * int[][] customTransitions) throws DecisionMethodException {
	 * this(categories,customRules,customTransitions); } public AbstractTree(
	 * String[] customRules, int[][] customTransitions) throws
	 * DecisionMethodException { this(); setRules(customRules);
	 * setTransitions(customTransitions); setChanged(); notifyObservers(); }
	 */
	/*
	 * TODO public AbstractTree(IDecisionMethod method) { method.getRules()
	 * method.g }
	 */
	protected abstract IDecisionRuleList initRules();

	protected void setRules(String[] customRules)
			throws DecisionMethodException {
		rules.setRules(customRules);
		setChanged();
		notifyObservers();
		if (changes != null) {
			changes.firePropertyChange("Rules", rules, null);
		}
	}

	protected abstract void setTransitions(int[][] customTransitions);

	/**
	 * returns the decision tree title (e.g. "Cramer rules")
	 */
	public String getTitle() {
		return name;

	}

	/**
	 * sets decision tree title (e.g. "Cramer rules")
	 */
	public void setTitle(String value) {
		this.name = value;

	}

	/**
	 * @see toxTree.core.IDecisionMethod#addDecisionRule(toxTree.core.IDecisionRule)
	 */
	public void addDecisionRule(IDecisionRule rule)
			throws DecisionMethodException {
		throw new DecisionMethodException("Not allowed!");

	}

	/**
	 * @see toxTree.core.IDecisionMethod#setDecisionRule(toxTree.core.IDecisionRule)
	 */
	public void setDecisionRule(IDecisionRule rule)
			throws DecisionMethodException {
		throw new DecisionMethodException("Not allowed!");
	}

	/**
	 * @see IDecisionMethod#explainRules(IDecisionResult,boolean)
	 */
	public StringBuffer explainRules(IDecisionResult result, boolean verbose)
			throws DecisionMethodException {
		try {
			StringBuffer b = result.explain(verbose);
			return b;
		} catch (DecisionResultException x) {
			throw new DecisionMethodException(x);
		}

	}

	protected boolean verifyResidues(IAtomContainerSet mols,
			IDecisionResult result, IDecisionRule rule)
			throws DecisionMethodException {
		logger.finer("Start processing residues\t"
				+ mols.getAtomContainerCount());
		boolean r = true;

		for (int i = 0; i < mols.getAtomContainerCount(); i++) {
			logger.finer("Residue\t" + Integer.toString(i + 1));
			try {
				MolAnalyser.analyse(mols.getAtomContainer(i));

				// r = r &&
				// verifyRules(mols.getAtomContainer(i),result,ruleIndex);
				r = r && verifyRules(mols.getAtomContainer(i), result, rule);
			} catch (Exception x) {
				throw new DecisionMethodException(x);
			}
		}
		logger.finer("Done processing residues.");
		return r;
	}

	/**
	 * {@link toxTree.core.IDecisionRule#verifyRule(IAtomContainer)}
	 */
	public boolean verifyRules(IAtomContainer mol, IDecisionResult result)
			throws DecisionMethodException {
		return verifyRules(mol, result, getTopRule());
	}

	/**
	 * abstract method, to be implemented in the child class
	 * 
	 * @param mol
	 *            - {@link org.openscience.cdk.interfaces.AtomContainer} to be
	 *            analyzed
	 * @param result
	 *            - {@link IDecisionResult} the result from the decision tree
	 *            application
	 * @param rule
	 *            - the starting rule
	 * @return
	 * @throws DecisionMethodException
	 */
	protected abstract boolean verifyRules(IAtomContainer mol,
			IDecisionResult result, IDecisionRule rule)
			throws DecisionMethodException;

	/**
	 * 
	 */
	public boolean classify(IAtomContainer mol, IDecisionResult result)
			throws DecisionMethodException {
		result.clear();
		result.setDecisionMethod(this);
		result.setEstimating();
		try {
			MolAnalyser.analyse(mol);
		} catch (MolAnalyseException x) {
			if (logger.isLoggable(Level.FINE))
				logger.log(Level.SEVERE, x.getMessage(), x);
			else
				logger.log(Level.SEVERE, x.getMessage());
			throw new DecisionMethodException(x);
		}
		categories.selectAll(false);
		boolean r = verifyRules(mol, result);
		// result.setEstimated(true);
		return r;
	}

	/**
	 * Can be used to display some options before applying the rules.
	 * 
	 * @param mol
	 */
	public abstract void setParameters(IAtomContainer mol);

	public JComponent optionsPanel(IAtomContainer atomContainer) {
		return EditorFactory.getInstance().optionsPanel(this, atomContainer);
		/*
		 * ArrayList<JComponent> components = new ArrayList<JComponent>(); for
		 * (int i=0;i< rules.size(); i++) { IDecisionRule rule =
		 * rules.getRule(i); if (rule instanceof IDecisionInteractive) {
		 * JComponent c = ((IDecisionInteractive)
		 * rule).optionsPanel(atomContainer); if (c != null) components.add(c);
		 * } } if (components.size()>0) { JPanel p = new JPanel(); JOutlookBar
		 * outlook = new JOutlookBar();
		 * outlook.setTabPlacement(JTabbedPane.LEFT); p.setLayout(new
		 * BoxLayout(p,BoxLayout.PAGE_AXIS)); for (int i=0; i <
		 * components.size();i++)
		 * outlook.add(components.get(i).toString(),components.get(i));
		 * p.add(outlook); //p.add(components.get(i)); return new
		 * PropertyEditor(atomContainer,new JScrollPane(p)); } else return null;
		 */
	}

	public int getNumberOfRules() {
		if (rules != null)
			return rules.size();
		else
			return 0;
	}

	/**
	 * @see toxTree.core.IDecisionMethod#getRule(int)
	 */
	public IDecisionRule getRule(int id) {
		if (rules == null)
			return null;
		return rules.getRule(id);
	}

	/**
	 * @see toxTree.core.IDecisionMethod#getRule(java.lang.String)
	 */
	public IDecisionRule getRule(String name) {
		if (rules == null)
			return null;
		for (int i = 0; i < rules.size(); i++)
			if (getRule(i).getTitle().equals(name))
				return getRule(i);
		return null;
	}

	/**
	 * 
	 * @param os
	 * @throws IOException
	 */
	public void printToStream(OutputStream os) throws IOException {
		DataOutputStream out = new DataOutputStream(os);
		for (int i = 0; i < rules.size(); i++) {
			out.writeChars(rules.getRule(i).toString());
			out.writeBytes("\n");
		}

	}

	/**
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public void printResults(String filename) throws IOException {
		FileOutputStream o = new FileOutputStream(filename);
		printToStream(o);
		o.close();
	}

	public boolean isFalseIfRuleNotImplemented() {
		return falseIfRuleNotImplemented;
	}

	public void setFalseIfRuleNotImplemented(boolean falseIfRuleNotImplemented) {
		this.falseIfRuleNotImplemented = falseIfRuleNotImplemented;
		setChanged();
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxTree.core.IDecisionMethod#addPropertyChangeListener(java.beans.
	 * PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		if (changes == null)
			changes = new PropertyChangeSupport(this);
		changes.addPropertyChangeListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * toxTree.core.IDecisionMethod#removePropertyChangeListener(java.beans.
	 * PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		if (changes != null)
			changes.removePropertyChangeListener(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		IDecisionMethod m = (IDecisionMethod) obj;
		if (getTitle().equals(m.getTitle())
				&& (getNumberOfClasses() == m.getNumberOfClasses())
				&& (getNumberOfRules() == m.getNumberOfRules())) {

			if (!categories.equals(m.getCategories()))
				return false;

			int nr = getNumberOfRules();
			if (nr != m.getNumberOfRules())
				return false;
			for (int i = 0; i < nr; i++) {
				// System.out.print(((DecisionNode)getRule(i)).getRule().getClass().getName());
				// System.out.println(((DecisionNode)getRule(i)).getRule().getClass().getName());
				if (!getRule(i).equals(m.getRule(i)))
					return false;
			}

			// TODO compare transitions
			return true;
		} else
			return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxTree.core.IDecisionMethod#getCategories()
	 */
	public IDecisionCategories getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxTree.core.IDecisionMethod#createDecisionResult()
	 */
	public IDecisionResult createDecisionResult() {
		IDecisionResult result = new TreeResult();
		result.setDecisionMethod(this);
		return result;
	}

	public int getNumberOfClasses() {
		return categories.size();
	}

	public IDecisionRuleList getRules() {
		return rules;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if (o == rules) { // if rules had chenged , fire change event for the
							// tree itself
			setChanged();
			notifyObservers();
		} else if (o instanceof IDecisionCategories) {
			setChanged();
			notifyObservers(o);
		}

	}

	public IDecisionRule getTopRule() {
		return getRule(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#setChanged()
	 */
	@Override
	protected synchronized void setChanged() {
		super.setChanged();
		// setModified(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#clearChanged()
	 */
	@Override
	protected synchronized void clearChanged() {
		super.clearChanged();
		// setModified(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxTree.core.IDecisionRule#isModified()
	 */
	public boolean isModified() {
		return modified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see toxTree.core.IDecisionRule#setModified(boolean)
	 */
	public void setModified(boolean value) {
		modified = value;
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		if (rules instanceof Observable)
			((Observable) rules).addObserver(this);
		if (categories instanceof Observable)
			((Observable) categories).addObserver(this);
		modified = false;
	}

	public IDecisionMethodEditor getEditor() {
		return EditorFactory.getInstance().createTreeEditor(this);
		// return new TreeEditorPanel(this);
		// return new EditTreeFrame(this);
	}

	public void fromXML(Element xml) throws XMLDecisionMethodException {
		// TODO Auto-generated method stub

	}

	public Element toShallowXML(Document document)
			throws XMLDecisionMethodException {
		Element m = document.createElement(xmltag_METHOD);
		m.setAttribute(xmltag_CLASS, getClass().getName());
		return m;
	}

	public Element toXML(Document document) throws XMLDecisionMethodException {
		Element e = document.createElement(XMLSerializable.xmltag_METHOD);

		e.setAttribute(xmltag_NAME, getTitle());
		Element explanation = document
				.createElement(XMLSerializable.xmltag_EXPLANATION);
		explanation.setTextContent(getExplanation());
		e.appendChild(explanation);
		e.setAttribute(xmltag_TREEROOT, Integer.toString(treeRoot));

		if (rules instanceof XMLSerializable)
			e.appendChild(((XMLSerializable) rules).toXML(document));
		else
			throw new XMLDecisionMethodException(rules.getClass().getName()
					+ " not XML serializable!");
		if (categories instanceof XMLSerializable)
			e.appendChild(((XMLSerializable) categories).toXML(document));
		else
			throw new XMLDecisionMethodException(categories.getClass()
					.getName() + " not XML serializable!");
		return e;
	}

	/*
	 * public void readExternal(ObjectInput arg0) throws IOException,
	 * ClassNotFoundException {
	 * 
	 * 
	 * } public void writeExternal(ObjectOutput out) throws IOException { try {
	 * DocumentBuilder builder =
	 * DocumentBuilderFactory.newInstance().newDocumentBuilder(); Document doc =
	 * builder.newDocument(); Element e = toXML(doc); doc.appendChild(e); Source
	 * source = new DOMSource(doc); Result result = new
	 * StreamResult((ObjectOutputStream)out); // Write the DOM document to the
	 * file Transformer xformer =
	 * TransformerFactory.newInstance().newTransformer();
	 * xformer.transform(source, result); } catch (Exception x) {
	 * x.printStackTrace(); } }
	 */
	public void setRules(IDecisionRuleList rules) {
		if ((this.rules != null) && (this.rules instanceof Observable))
			((Observable) rules).deleteObserver(this);
		this.rules = rules;
		if ((rules != null) && (rules instanceof Observable))
			((Observable) rules).addObserver(this);
	}

	public void setCategories(IDecisionCategories categories) {
		if ((this.categories != null)
				&& (this.categories instanceof Observable))
			((Observable) categories).deleteObserver(this);
		this.categories = categories;
		if ((categories != null) && (categories instanceof Observable))
			((Observable) categories).addObserver(this);

	}

	public synchronized int getPriority() {
		return priority;
	}

	public synchronized void setPriority(int priority) {
		this.priority = priority;
	}

	public int testRulesWithSelector() throws Exception {
		int nr = getNumberOfRules();
		int na = 0;
		for (int i = 0; i < nr; i++) {
			IDecisionRule rule = rules.getRule(i);
			logger.log(Level.FINE,rule.getClass().getName());
			if (rule.getSelector() == null) {

				na++;
			} else {
				IAtomContainer a = null;
				try {
					a = rule.getExampleMolecule(true);
					if (a == null)
						continue;
				} catch (Exception x) {
					logger.log(Level.WARNING, x.getMessage(), x);
					continue;
				}
				IChemObjectSelection hit = rule.getSelector().process(a);

			}
		}
		return na;
	}

	public void walkRules(IDecisionRule rule, IProcessRule processor)
			throws DecisionMethodException {
		ArrayList<Integer> visited = new ArrayList<Integer>();
		processor.init(this);
		walkRules(rule, visited, processor);
		processor.done();
	}

	protected void walkRules(IDecisionRule rule, ArrayList<Integer> visited,
			IProcessRule processor) throws DecisionMethodException {
		if (rule == null)
			return;
		else {
			if (visited.indexOf(rule.getNum()) == -1) {
				visited.add(rule.getNum());
				processor.process(this, rule);

				final boolean[] answers = { true, false };
				for (int i = 0; i < answers.length; i++) {
					IDecisionRule nextrule = getBranch(rule, answers[i]);
					walkRules(nextrule, visited, processor);
				}
			} else
				return; // visited
		}

	}

	public BufferedImage getImage(IAtomContainer mol) throws AmbitException {
		return getImage(mol, null, 150, 150, false);
	}

	public BufferedImage getImage(IAtomContainer mol, String ruleID, int width,
			int height, boolean atomnumbers) throws AmbitException {
		IDecisionRuleList rules = getRules();
		IAtomContainerHighlights selector = null;

		for (int i = 0; i < rules.size(); i++) {
			IDecisionRule rule = rules.get(i);
			if (ruleID == null) {
				if (selector == null)
					selector = new TreeSelector();
				((TreeSelector) selector).addSelector(rule.getSelector());

			} else if (rule.getID().equals(ruleID)) {
				selector = rule.getSelector();
				break;
			}
		}
		CompoundImageTools tools = new CompoundImageTools(new Dimension(width,
				height));
		return tools.getImage(mol, selector, true, atomnumbers);
	}

	public void setWeb(Boolean web) {
		this.web = web;

	}

	public boolean isWeb() {
		return web;
	}

	@Override
	public BufferedImage getLegend(int width, int height) throws AmbitException {
		BufferedImage buffer = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffer.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		RenderingHints rh = g.getRenderingHints();
		rh.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.addRenderingHints(rh);
		IDecisionCategories c = getCategories();
		if ((c == null) || (c.size() == 0))
			return buffer;
		int h = height / c.size();
		g.setFont(new Font("Arial", Font.BOLD, h / 4 == 0 ? 12 : h / 4));
		CategoriesRenderer r = new CategoriesRenderer(c);

		for (int i = 0; i < c.size(); i++) {
			IDecisionCategory cat = c.get(i);
			g.setBackground(r.getShowColor(i));
			g.setColor(r.getShowColor(i));
			g.fillRect(3, 3 + h * i, h - 6, h - 6);
			g.setColor(Color.black);

			g.drawString(cat.getName(), h + 10, 3 + h / 2 + h * i);
		}
		return buffer;
	}

	protected String retrieveExplanation(String resourceBundle) {
		ResourceBundle labels = ResourceBundle.getBundle(resourceBundle,
				Locale.ENGLISH, getClass().getClassLoader());
		StringBuilder b = new StringBuilder();

		for (int i = 1; i < 100; i++) {
			String key = String.format("reference%d", i);
			if (labels.containsKey(key)) {
				String ref = labels.getString(key);
				if (ref.startsWith("http"))
					b.append(String.format("<li><a href='%s'>%s</a>", ref, ref));
				else
					b.append(String.format("<li>%s", ref));
			} else
				break;
		}
		String vendor = labels.getString("vendor");
		String vendoruri = labels.getString("vendoruri");
		String specVendor = "";
		try {
			specVendor = labels.getString("specification_vendor");
			if (specVendor != null && !"".equals(specVendor))
				specVendor = "<h5>Specification: " + specVendor + "</h5>";
		} catch (Exception x) {
			specVendor = "";
		}
		String uri = labels.getString("uri");

		String traindata = labels.containsKey("trainingdata") ? String.format(
				"<h5>Training data: <a href='%s'>%s</a></h5>",
				labels.getString("trainingdata"), "Click to retrieve") : "";

		String testdata = labels.containsKey("testdata") ? String.format(
				"<h5>Test data: <a href='%s'>%s</a></h5>",
				labels.getString("testdata"), "Click to retrieve") : "";

		return String
				.format("<html><body><h3>%s</h3><h5>WWW: <a href='%s'>%s</a></h5><h5>Vendor: <a href='%s'>%s</a></h5>%s%s%s<hr><h3>References:</h3><ol>%s</ol></body></html>",
						labels.getString("explanation"), uri, uri, vendoruri,
						vendor, specVendor, traindata, testdata, b.toString());

	}

}

class TreeSelector implements IAtomContainerHighlights {
	protected List<IAtomContainerHighlights> selectors = new ArrayList<IAtomContainerHighlights>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 7753309179379127408L;

	public void addSelector(IAtomContainerHighlights selector) {
		selectors.add(selector);
	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public IChemObjectSelection process(IAtomContainer target)
			throws AmbitException {
		Collection<IAtomContainer> set = new ArrayList<IAtomContainer>();
		MultiSelection<IAtomContainer> ms = new MultiSelection<IAtomContainer>(
				set);
		for (IAtomContainerHighlights selector : selectors)
			try {
				set.add(selector.process(target).getConnectedAtomContainer());
			} catch (Exception x) {
				throw new AmbitException(x);
			}
		return ms;
	}

	@Override
	public void setEnabled(boolean value) {
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public void open() throws Exception {
	}

}