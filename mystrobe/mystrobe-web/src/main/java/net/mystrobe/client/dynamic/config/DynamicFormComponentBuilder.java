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
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.dynamic.page.AbstractSelectRecordModalPanel;
import net.mystrobe.client.dynamic.page.SelectRecordModalPanel;
import net.mystrobe.client.dynamic.panel.AutoCompleteTextFieldPanel;
import net.mystrobe.client.dynamic.panel.CheckBoxPanel;
import net.mystrobe.client.dynamic.panel.DateFieldPanel;
import net.mystrobe.client.dynamic.panel.DropDownPanel;
import net.mystrobe.client.dynamic.panel.DynamicFormComponentPanel;
import net.mystrobe.client.dynamic.panel.RadioPanel;
import net.mystrobe.client.dynamic.panel.SelectableTextFieldPanel;
import net.mystrobe.client.dynamic.panel.TextAreaPanel;
import net.mystrobe.client.dynamic.panel.TextFieldPanel;

import org.apache.wicket.model.IModel;

public class DynamicFormComponentBuilder implements IDynamicFormComponentBuilder {

	@Override
	public <T extends Serializable> DynamicFormComponentPanel<T> createTextFieldPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap ) {
		
		return new TextFieldPanel<T>(id, model, propertyName, labelModel, required, readOnly, 0);
	}
	
	public <T extends Serializable, M extends IDataBean> DynamicFormComponentPanel<T> createSelectablePanel(
			String id, IModel<T> model, String propertyName, IModel<String> labelModel, 
			boolean required, boolean readOnly, Map<IDynamicFormFieldConfig.Property, ?> configurationMap ) {
		
		IDataObject<M> linkedDataSource = (IDataObject<M>) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedDataObject);
		String linkedColumnName  = (String) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedColumnName);
		
		Class<AbstractSelectRecordModalPanel<M>> selecatbleRecordModalPanelClass  = (Class<AbstractSelectRecordModalPanel<M>>) configurationMap.get(IDynamicFormFieldConfig.Property.SelectableModalWindowPanelClass);
		
		if( selecatbleRecordModalPanelClass == null ) {
			selecatbleRecordModalPanelClass = (Class) SelectRecordModalPanel.class; 
		}
		
		String visibleColumnName = null;
		if (configurationMap.containsKey(IDynamicFormFieldConfig.Property.VisibleColumnName)) {
			visibleColumnName = (String) configurationMap.get(IDynamicFormFieldConfig.Property.VisibleColumnName);
		}
		
		String descriptionColumn = null;
		if (configurationMap.containsKey(IDynamicFormFieldConfig.Property.DescriptionColumn)) {
			descriptionColumn = (String) configurationMap.get(IDynamicFormFieldConfig.Property.DescriptionColumn);
		}
		
		IDynamicFormConfig<M> selectRecordWindowTableConfig = null;
		if (configurationMap.containsKey(IDynamicFormFieldConfig.Property.SelectRecordTableConfig)) {
			selectRecordWindowTableConfig = (IDynamicFormConfig<M>) configurationMap.get(IDynamicFormFieldConfig.Property.SelectRecordTableConfig);
		}
		
		return new SelectableTextFieldPanel<T, M, AbstractSelectRecordModalPanel<M>>(
				id,  model, propertyName, labelModel, required, readOnly, linkedDataSource,
				linkedColumnName, selecatbleRecordModalPanelClass, visibleColumnName,
				descriptionColumn, selectRecordWindowTableConfig);
	}
	

	@Override
	public DynamicFormComponentPanel<Boolean> createCheckBoxPanel(
			String id, IModel<Boolean> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly, 
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap) {
		
		return new CheckBoxPanel(id, model, propertyName, labelModel, required, readOnly);
	}

	@Override
	public <T extends Serializable, M extends IDataBean> DynamicFormComponentPanel<T> createAuoCompleteTextFieldPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap) {
		
		IDataObject<M> autoCompleteDataSource = (IDataObject<M>) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedDataObject);
		String autoCompleteFilterColumnName  = (String) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedColumnName);
		return new AutoCompleteTextFieldPanel<T,M>(id, model, propertyName, labelModel, 
				required, readOnly, autoCompleteDataSource, autoCompleteFilterColumnName);
	}

	@Override
	public <T extends Serializable> DynamicFormComponentPanel<T> createTextAreaPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap) {
		
		return new TextAreaPanel<T>(id, model, propertyName, labelModel, required, readOnly, 0);
	}

	@Override
	public <T extends Serializable> DynamicFormComponentPanel<T> createRadioPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap) {
		
		Collection<IFieldValue<T>> options = (Collection<IFieldValue<T>>) configurationMap.get(IDynamicFormFieldConfig.Property.ValuesList);
		return new RadioPanel<T>(id, model, propertyName, labelModel, options, false, required, readOnly);
	}

	@Override
	public <T extends Serializable> DynamicFormComponentPanel<T> createDropDownPanel(
			String id, IModel<T> model, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap) {
		
		Collection<IFieldValue<T>> options = (Collection<IFieldValue<T>>) configurationMap.get(IDynamicFormFieldConfig.Property.ValuesList);
		return new DropDownPanel<T>(id, model, propertyName, labelModel, options, required, readOnly);
	}

	@Override
	public DynamicFormComponentPanel<Date> createDateFieldPanel(
			String id, IModel<Date> dateModel, String propertyName,
			IModel<String> labelModel, boolean required, boolean readOnly,
			Map<IDynamicFormFieldConfig.Property, ?> configurationMap) {
		
		String format = (String) configurationMap.get(IDynamicFormFieldConfig.Property.Format);
		return new DateFieldPanel(id, dateModel, propertyName, labelModel, required, readOnly, format);
	}
	
	

}
