/**
 * Copyright (C) 2010-2011 TVH Group NV. <kalman.tiboldi@tvh.com>
 *
 * This file is part of the MyStroBe project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package net.mystrobe.client.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IFilterParameter;

/**
 * @author TVH Group NV
 */
public class FilterParameter implements IFilterParameter, Serializable {

	
	private static final long serialVersionUID = -5563911514444078779L;
	
	private String column;
	private FilterOperator operator;
	private Serializable value;

	public FilterParameter(String column, FilterOperator operator, Serializable value) {
		this.column = column;
		this.operator = operator;
		this.value = value;
	}
	
	public FilterParameter(Object column, FilterOperator operator, Serializable value) {
		this( column.toString(), operator,  value);
	}
	
	
	public String getColumn() {
		return column;
	}

	public FilterOperator getOperator() {
		return operator;
	}

	public Serializable getValue() {
		return value;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public void setOperator(FilterOperator filterOperator) {
		this.operator = filterOperator;
	}

	public void setValue(Serializable filterValue) {
		this.value = filterValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterParameter other = (FilterParameter) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FilterParameter [column=" + column + ", operator=" + operator
				+ ", value=" + value + "]";
	}
	
	
	
	public static Set<IFilterParameter> createFilterSet(String column, FilterOperator operator, String value) {
		if( column == null ) throw new IllegalArgumentException("Filter column can not be null");
		
		HashSet<IFilterParameter> result = new HashSet<IFilterParameter>(5);
		result.add(new FilterParameter(column, operator, value));
		return result;
	}
	
	
	public static Set<IFilterParameter> createFilterSet(Object column, FilterOperator operator, Object value) {
		return FilterParameter.createFilterSet(
					column.toString()
					, operator
					, value != null ? value.toString() : ""); 
	}
	
}
