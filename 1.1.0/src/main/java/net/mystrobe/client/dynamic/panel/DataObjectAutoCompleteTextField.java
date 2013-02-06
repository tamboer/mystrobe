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
 package net.mystrobe.client.dynamic.panel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.mystrobe.client.CursorStates;
import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.impl.FilterParameter;
import net.mystrobe.client.navigator.DataObjectIterator;
import net.mystrobe.client.ui.UICssResourceReference;
import net.mystrobe.client.util.DataBeanUtil;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;

/**
 * @author TVH Group NV
 */
public class DataObjectAutoCompleteTextField<S extends IDataBean, T> extends AutoCompleteTextField<T> {

	private static final long serialVersionUID = -748028589553195019L;

	protected IDataObject<S> linkedDataObject;
	
	protected String linkedColumnName;
	
	public DataObjectAutoCompleteTextField(String id, IModel<T> model, IDataObject<S> dataObject, String columnName) {
		super(id, model);
		this.linkedDataObject = dataObject;
		this.linkedColumnName = columnName;
	}

	@Override
	protected Iterator<T> getChoices(String input) {
		
		IFilterParameter filterParameter = new FilterParameter(linkedColumnName, FilterOperator.BEGINS, input);
		
		linkedDataObject.clearFilters();
		linkedDataObject.addFilter(filterParameter);
		linkedDataObject.resetDataBuffer();
		
		Set<T> values = new HashSet<T>();
		
		if (!linkedDataObject.getCursorState().equals(CursorStates.NoRecordAvailable)) {
			DataObjectIterator<S> linkedDataIterator = new DataObjectIterator<S>(linkedDataObject);
			while (linkedDataIterator.hasNext()) {
				values.add((T)DataBeanUtil.getFieldValue(linkedDataIterator.next(), this.linkedColumnName, null));
			}
		}
		
		return values.iterator();
	}
	
}
