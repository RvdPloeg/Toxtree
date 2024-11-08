/*
Copyright Ideaconsult Ltd. (C) 2005-2007 

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
package toxTree.exceptions;

import toxTree.core.XMLSerializable;

/**
 * Thrown when processing {@link XMLSerializable}.
 * @author Nina Jeliazkova
 *
 */
public class XMLDecisionMethodException extends DecisionMethodException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4090629078836210269L;

	public XMLDecisionMethodException() {
		super();
		
	}

	public XMLDecisionMethodException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public XMLDecisionMethodException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public XMLDecisionMethodException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}


