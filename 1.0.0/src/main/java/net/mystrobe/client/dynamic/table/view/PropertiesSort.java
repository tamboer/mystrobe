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
 package net.mystrobe.client.dynamic.table.view;

import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.SortState;
import net.mystrobe.client.SortState.Sort;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;


/**
 * Multiple properties sort state locator class.
 * 
 * Implementation is required by the order link wicket implementation.
 * 
 * @author TVH Group NV
 */
public abstract class PropertiesSort implements ISortStateLocator {
	
	private static final long serialVersionUID = -1601840873369844491L;
	
	private PropertiesSortState  propertiesSortStates = new PropertiesSortState();
	

	public ISortState getSortState() {
		return propertiesSortStates;
	}

					
	public void setSortState(ISortState state) {
		propertiesSortStates = (PropertiesSortState) state;
	}
	
	public void addSortableProperty(String property, SortOrder sortState) {
		propertiesSortStates.addSortableProperty(property, sortState );
	}
	
	public abstract void onChangeSortState(SortState sortState);
	
	
	/**
	 * Multiple properties sort state class.
	 * 
	 * Implementation is required by the order link wicket implementation.
	 */
	protected class PropertiesSortState implements ISortState, Cloneable {
	
		private static final long serialVersionUID = 3344772787685750317L;
		
		private Map<String, SortOrder> propertiesSortStateMap = new HashMap<String, SortOrder>(); 
		
		public SortOrder getPropertySortOrder(String property) {
			return propertiesSortStateMap.containsKey(property) ? 
					propertiesSortStateMap.get(property) : SortOrder.NONE;
		}
	
		public void setPropertySortOrder(String property, SortOrder order) {
			propertiesSortStateMap.put(property, order );
			
			for (String sortProperty : propertiesSortStateMap.keySet()) {
				if (!property.equals(sortProperty)) {
					propertiesSortStateMap.put(sortProperty, SortOrder.NONE);
				}
			}
			
			Sort sortOrder = SortOrder.ASCENDING.equals(order) ? Sort.Ascending : Sort.Descending; 
			
			SortState sortState = new SortState(property, sortOrder);
			
			onChangeSortState(sortState);
		}
		
		public void addSortableProperty(String property, SortOrder sortState) {
			propertiesSortStateMap.put(property, sortState );
		}
	}
}	
