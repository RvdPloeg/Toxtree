/*
Copyright Ideaconsult Ltd. (C) 2005-2007  
Contact: nina@acad.bg

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/


package toxTree.data;

import toxTree.core.IDecisionCategory;
import toxTree.exceptions.FilterException;

public class CategoryFilter extends ObjectPropertyFilter {

	public CategoryFilter(Object property, IDecisionCategory category) throws FilterException {
		super(property,category.toString());
	}
	public CategoryFilter(Object property, String category) throws FilterException {
		super(property,category);
	}	
	@Override
	public String toString() {
		return getTag() + " = " + getValue().toString();
	}
}


