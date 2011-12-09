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
 package net.mystrobe.client.dynamic.config;

import net.mystrobe.client.WicketDSRuntimeException;

/**
 * @author TVH Group NV
 */
public class FieldValue<T> implements IFieldValue<T> {
	
	private static final long serialVersionUID = -7106446124148678014L;

	private String label;
	
	private T value;
	
	private Comparable sortValue;

	public FieldValue() {
		super();
	}

	public FieldValue(String label, T value, Comparable sortValue) {
		super();
		
		if (value == null) {
			throw new WicketDSRuntimeException("Value parameter can not be null");
		}
		
		if (label == null) {
			throw new WicketDSRuntimeException("Label parameter can not be null");
		}
		
		this.label = label;
		this.value = value;
		this.sortValue = sortValue;
	}

	public String getLabel() {
		return label;
	}

	public T getValue() {
		return value;
	}
	
	public Comparable getSortValue() {
		return sortValue;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public void setValue(T value) {
		this.value = value;
	}

	public void setSortValue(Comparable sortValue) {
		this.sortValue = sortValue;
	}

	@Override
	public int compareTo(IFieldValue<T> o) {
		if (o == null) {
			return 1;
		}
		
		if (o.getSortValue() == null) {
			return 1;
		}
		
		if (this.getSortValue() == null) {
			return -1;
		}
		
		return this.getSortValue().compareTo(o.getSortValue());
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj == null)
	            return false;
        if (obj == this)
            return true;
       
        if (!(obj.getClass() != getClass()))
            return false;
        
        FieldValue<T> o = (FieldValue<T>) obj;
        
		return this.getValue().equals(o.getValue()) ;
	}

	@Override
	public int hashCode() {
		return this.getValue().hashCode();
	}
	
	
}
