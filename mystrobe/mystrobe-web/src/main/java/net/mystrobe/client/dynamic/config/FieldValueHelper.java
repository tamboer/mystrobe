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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * @author TVH Group NV
 */
public class FieldValueHelper {
	
	/**
	 * Build a drop down from list of {@link IFieldValue}.  
	 * 
	 * @param <T> Drop down and model object type.
	 * @param id drop down markup id.
	 * @param model Model instance.
	 * @param fieldValues Collection of {@link IFieldValue} for the drop down optinos.
	 *  
	 * @return New drop down component.
	 */
	public static <T> DropDownChoice<T> buildFieldValueDropDownChoice(String id, IModel<T> model, Collection<IFieldValue<T>> fieldValues) {
		
		List<T> optionValuesList = new ArrayList<T>(fieldValues.size());
		
		for (IFieldValue<T> fieldValue : fieldValues) {
			optionValuesList.add(fieldValue.getValue());
		}
		
		return new DropDownChoice<T>(id, model, optionValuesList, new FieldValueOptionsRenderer<T>(fieldValues));
	}
	
	/**
	 * Build a drop down from list of {@link IFieldValue}.  
	 * 
	 * @param <T> Drop down and model object type.
	 * @param id drop down markup id.
	 * @param model Model instance.
	 * @param fieldValues Collection of {@link IFieldValue} for the drop down optinos.
	 *  
	 * @return New drop down component.
	 */
	@Deprecated 
	public static <T> DropDownChoice<T> buildFiledValueDropDownChoice(String id, IModel<T> model, Collection<IFieldValue<T>> fieldValues) {
		return buildFieldValueDropDownChoice(id, model, fieldValues);
	}

}
