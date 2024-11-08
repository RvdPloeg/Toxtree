/*
Copyright Ideaconsult Ltd. (C) 2005-2011 

Contact: jeliazkova.nina@gmail.com

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
package toxTree.tree.rules;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import net.idea.modbcum.i.exceptions.AmbitException;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;
import org.openscience.cdk.renderer.selection.SingleSelection;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import toxTree.core.IDecisionInteractive;
import toxTree.core.IImplementationDetails;
import toxTree.exceptions.DRuleNotImplemented;
import toxTree.exceptions.DRulePropertyNotAvailable;
import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.AbstractRule;
import toxTree.ui.EditorFactory;
import ambit2.core.data.MoleculeTools;
import ambit2.rendering.IAtomContainerHighlights;

/**
 * 
 * Verifies if property is >, < or = to a {@link #getProperty()} value. Property
 * to be read as {@link IAtomContainer}.getProperty({@link #getPropertyName()}).
 * If there exist no such property of the molecule, a
 * {@link #inputProperty(IAtomContainer)} method is invoked, which typically
 * waits for user input of the property value. If the property is not assigned
 * after {@link #inputProperty(IAtomContainer)} call, then
 * {@link DRuleNotImplemented} is fired.
 * 
 * @author Nina Jeliazkova nina@acad.bg
 * @author Martin Martinov <b>Modified</b> Dec 17, 2006
 */
public class RuleVerifyProperty extends AbstractRule implements
		IDecisionInteractive, IImplementationDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2634387005293933539L;
	protected Double propertyStaticValue = 0.0;
	protected String propertyName = "property";
	protected String propertyUnits = "";
	protected UserOptions options = UserOptions.YES;

	public UserOptions getOptions() {
		return options;
	}

	public void setOptions(UserOptions options) {
		this.options = options;
	}

	public static String condition_higher = ">";
	public static String condition_lower = "<";
	public static String condition_equals = "=";
	protected String condition = condition_equals;
	protected static NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
	protected double[] propertyExamples = new double[] { Double.NaN, Double.NaN };
	protected PropertyChangeListener listener;

	public PropertyChangeListener getListener() {
		return listener;
	}

	public void setListener(PropertyChangeListener listener) {
		this.listener = listener;
	}

	public RuleVerifyProperty() {
		super();
		nf.setMaximumFractionDigits(4);
		setListener(EditorFactory.getInstance().createPropertyInput());
	}

	public RuleVerifyProperty(String propertyName, String units,
			String condition, double value) {
		this();
		setPropertyName(propertyName);
		setPropertyUnits(units);
		setCondition(condition);
		setProperty(value);
		setTitle(getCaption());
	}

	public String getCaption() {
		return getPropertyName() + "[" + getPropertyUnits() + "] "
				+ getCondition() + " " + nf.format(getProperty());
	}
	@Override
	public boolean verifyRule(IAtomContainer mol)
			throws DecisionMethodException {
		logger.fine(toString());
		try {
			Object value = mol.getProperty(this.propertyName);

			if ((value == null) || ("".equals(value))) {
				if (getInteractive()) {
					value = inputProperty(mol);
					mol.setProperty(propertyName, value.toString());
				} else
					throw new DRuleNotImplemented(propertyName
							+ " not assigned ");
			}
			// Double.valueOf returns Double, perhaps Double.parseDouble() could
			// be used
			Number number = NumberFormat.getNumberInstance(Locale.ENGLISH)
					.parse(value.toString());
			return this.compare(number.doubleValue(), this.propertyStaticValue);
		} catch (ParseException x) {
			throw new DRulePropertyNotAvailable(propertyName, propertyName
					+ " invalid value ", x);
		} catch (NumberFormatException x) {
			// just in case, the property might hold any value, or be empty
			throw new DRulePropertyNotAvailable(propertyName, propertyName
					+ " invalid value ", x);
		} catch (NullPointerException x) {
			// or the getProperty might be null
			throw new DRulePropertyNotAvailable(propertyName, propertyName
					+ " not assigned ", x);
		}
	}

	public String inputProperty(IAtomContainer mol)
			throws DecisionMethodException {
		Object value = mol.getProperty(this.propertyName);

		if ((value == null) || ("".equals(value))) {
			if (getListener() != null) {
				getListener().propertyChange(
						new PropertyChangeEvent(this, propertyName, null, mol));
				value = mol.getProperty(this.propertyName);
			} else
				throw new DRuleNotImplemented(String.format(
						"%s %s not assigned ", propertyName, propertyUnits));
		}
		return value.toString();
	}

	public void setProperty(double propertyStaticValue) {
		this.propertyStaticValue = propertyStaticValue;
	}

	public double getProperty() {
		return this.propertyStaticValue;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return this.condition;
	}

	public boolean compare(double Param1, double Param2) {

		if (this.getCondition().equals(condition_higher))
			return Param1 > Param2;
		else if (this.getCondition().equals(condition_lower))
			return Param1 < Param2;
		else
			// better introduce parameter for equality
			return Param1 == Param2;
	}

	@Override
	public boolean isImplemented() {
		return true;
	}

	@Override
	public String getExplanation() {
		return getPropertyName() + " " + getCondition() + " " + getProperty();
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public IAtomContainer getExampleMolecule(boolean ruleResult)
			throws DecisionMethodException {
		int index = 0;
		if (ruleResult)
			index = 1;
		SmilesParser p = new SmilesParser(SilentChemObjectBuilder.getInstance());
		try {
			IAtomContainer m = p.parseSmiles(examples[index]);
			m.setProperty(getPropertyName(), propertyExamples[index]);
			return m;
		} catch (Exception x) {
			throw new DecisionMethodException(x);
		}
	}

	/*
	 * @Override public IDecisionRuleEditor getEditor() { return new
	 * RulePropertyEditor(this); }
	 */
	/**
	 * public String inputProperty(IAtomContainer mol) {
	 * 
	 * PropertyEditor p = new PropertyEditor(mol,new
	 * OptionsPanel(toString(),mol, this)); if
	 * (JOptionPane.showConfirmDialog(null,p,"Rule " + getID() + "." +
	 * getCaption(), JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) ==
	 * JOptionPane.OK_OPTION) { setInteractive(!p.isSilent()); return
	 * p.getPropertyValue(); } else return null; //return
	 * JOptionPane.showInputDialog("Enter " + propertyName + "," +
	 * propertyUnits); }
	 */
	public String getPropertyUnits() {
		return propertyUnits;
	}

	public void setPropertyUnits(String propertyUnits) {
		this.propertyUnits = propertyUnits;
	}

	public boolean getInteractive() {
		return options.isInteractive();
	}

	public void setInteractive(boolean value) {
		setOptions(options.setInteractive(value));
	}

	/*
	 * public JComponent optionsPanel(IAtomContainer atomContainer) {
	 * 
	 * if (options.isInteractive() && ((atomContainer==null) ||
	 * (atomContainer.getProperty(this.propertyName) == null))) { return new
	 * OptionsPanel(toString(),atomContainer, this); } else return null; }
	 */
	public String getImplementationDetails() {
		StringBuffer b = new StringBuffer();
		b.append("The value is expected to be assigned as a molecule property, available by IAtomContainer.getProperty(\"");
		b.append(propertyName);
		b.append("\".\n");
		if (!"".equals(propertyUnits)) {
			b.append("units: ");
			b.append(propertyUnits);
		}
		b.append("The value could be assigned by e.g. reading a .csv file with column \"");
		b.append(propertyName);
		b.append("\" or .sdf file with property <");
		b.append(propertyName);
		b.append(">\n");
		b.append("If value is not assigned, the user is prompted to enter the value interactively.");
		return b.toString();
	}

	public void removeListener() {
		this.listener = null;

	}

	public IAtomContainerHighlights getSelector() {
		return new IAtomContainerHighlights() {
			/**
		     * 
		     */
			private static final long serialVersionUID = -959872394651597110L;

			public IChemObjectSelection process(IAtomContainer mol)
					throws AmbitException {
				try {
					IAtomContainer selected = MoleculeTools
							.newAtomContainer(SilentChemObjectBuilder
									.getInstance());
					if (verifyRule(mol)) {
						selected.add(mol);
						return new SingleSelection<IAtomContainer>(selected);
					} else
						return null;
				} catch (DecisionMethodException x) {
					throw new AmbitException(x);
				}
			}

			public boolean isEnabled() {
				return true;
			}

			public long getID() {
				return 0;
			}

			public void setEnabled(boolean arg0) {
			}

			@Override
			public void open() throws Exception {
			}

			@Override
			public void close() throws Exception {
			}

		};
	}
}
