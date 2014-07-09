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

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.dynamic.panel.DynamicFormComponentPanel;

import org.apache.wicket.model.IModel;

public interface IDynamicFormComponentBuilder {
	
	public <T extends Serializable> DynamicFormComponentPanel<T> createTextFieldPanel(String id, 
			IModel<T> model, String propertyName, IModel<String> labelModel, 
			boolean required, boolean readOnly, Map<IDynamicFormFieldConfig.Property, ?> configurationMap ); 

	public <T extends Serializable, M extends IDataBean> DynamicFormComponentPanel<T> createSelectablePanel(
			String id, IModel<T> model, String propertyName, IModel<String> labelModel, 
			boolean required, boolean readOnly,  Map<IDynamicFormFieldConfig.Property, ?> configurationMap);
	
	public DynamicFormComponentPanel<Boolean> createCheckBoxPanel(
			String id, IModel<Boolean> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly, 
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap);
	
	public <T extends Serializable, M extends IDataBean> DynamicFormComponentPanel<T> createAuoCompleteTextFieldPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap);
	
	public <T extends Serializable> DynamicFormComponentPanel<T> createTextAreaPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap);
	
	public <T extends Serializable> DynamicFormComponentPanel<T> createRadioPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap); 
	
	public <T extends Serializable> DynamicFormComponentPanel<T> createDropDownPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap); 

	public DynamicFormComponentPanel<Date> createDateFieldPanel(
			String id, IModel<Date> dateModel, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap);

}
