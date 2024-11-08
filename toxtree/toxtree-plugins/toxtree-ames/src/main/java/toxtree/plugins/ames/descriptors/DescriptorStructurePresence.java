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

package toxtree.plugins.ames.descriptors;

import java.util.logging.Logger;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.BooleanResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import ambit2.smarts.query.ISmartsPattern;
import ambit2.smarts.query.SMARTSException;

public abstract class DescriptorStructurePresence<T> implements
		IMolecularDescriptor {

	protected transient static Logger logger = Logger
			.getLogger(DescriptorStructurePresence.class.getName());

	protected String[] paramNames = { "fragment", "resultName" };

	protected ISmartsPattern<T> fragment = null;
	protected String resultName = "I";

	public DescriptorStructurePresence() {
		super();
		fragment = createSmartsPattern();
	}

	public String getSMARTS() {
		if (fragment == null)
			return null;
		else
			return fragment.getSmarts();
	}

	protected abstract ISmartsPattern<T> createSmartsPattern();

	public void setSMARTS(String smarts) throws SMARTSException {
		fragment.setSmarts(smarts);
	}

	public DescriptorValue calculate(IAtomContainer container) {

		if (fragment == null)
			return new DescriptorValue(getSpecification(), getParameterNames(),
					getParameters(), new BooleanResult(false),
					new String[] { getResultName() }, new CDKException(
							"Substructure not assigned!"));

		try {
			boolean ok = fragment.match(container) > 0;
			return new DescriptorValue(getSpecification(), getParameterNames(),
					getParameters(), new BooleanResult(ok),
					new String[] { getResultName() });
		} catch (Exception x) {
			return new DescriptorValue(getSpecification(), getParameterNames(),
					getParameters(), new BooleanResult(false),
					new String[] { getResultName() }, x);
		}

	}

	public IDescriptorResult getDescriptorResultType() {
		return new BooleanResult(true);
	}

	public String[] getParameterNames() {
		return paramNames;
	}

	public Object getParameterType(String name) {
		if (paramNames[0].equals(name))
			return new QueryAtomContainer(SilentChemObjectBuilder.getInstance());
		else if (paramNames[1].equals(name))
			return getResultName();
		else
			return null;
	}

	public Object[] getParameters() {
		return new Object[] { getSMARTS(), resultName };
	}

	public DescriptorSpecification getSpecification() {
		return new DescriptorSpecification(
				"ToxTree Mutant plugin",
				this.getClass().getName(),
				"$Id: DescriptorStructurePresence.java  2007-04-07 19:41 nina$",
				"ToxTree Mutant plugin");
	}

	public void setParameters(Object[] params) throws CDKException {
		if (params.length > 2)
			throw new CDKException(getClass().getName()
					+ " expects 2 parameters only.");
		if (params.length >= 1)
			if (params[0] instanceof String)
				try {
					setSMARTS(params[0].toString());
				} catch (SMARTSException x) {
					throw new CDKException(x.getMessage());
				}
			else
				throw new CDKException(params[0]
						+ " not an instance of IQueryAtomContainer");
		if (params.length == 2)
			setResultName(params[1].toString());
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public String getResultName() {
		return resultName;
	}

	@Override
	public String toString() {
		return resultName;
	}

	public String[] getDescriptorNames() {
		return new String[] { getResultName() };
	}

	@Override
	public void initialise(IChemObjectBuilder builder) {

	}
}
