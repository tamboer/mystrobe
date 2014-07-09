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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author TVH Group NV
 */
public class FieldValueOptionsRenderer<T> implements IChoiceRenderer<T> {

	private static final long serialVersionUID = 2824032184986659237L;

	private Map<T, IFieldValue<T>> optionsMap;
	
	public FieldValueOptionsRenderer(Map<T, IFieldValue<T>> optionsMap) {
		super();
		this.optionsMap = optionsMap;
	}
	
	public FieldValueOptionsRenderer(Collection<IFieldValue<T>> options) {
		super();
		this.optionsMap = new HashMap<T, IFieldValue<T>>();
		for (IFieldValue<T> option : options) {
			this.optionsMap.put(option.getValue(), option);
		}
	}

	public Object getDisplayValue(T object) {
		IFieldValue<T> fieldValue = optionsMap.get(object);
		return fieldValue.getLabel();
	}

	public String getIdValue(T object, int index) {
		return object.toString();
	}
}
