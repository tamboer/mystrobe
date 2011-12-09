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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.CursorStates;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.dynamic.config.FieldValue;
import net.mystrobe.client.dynamic.config.FieldValueOptionsRenderer;
import net.mystrobe.client.dynamic.config.IFieldValue;
import net.mystrobe.client.navigator.DataObjectIterator;
import net.mystrobe.client.util.DataBeanUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Drop down dynamic panel.
 * 
 * Class is used to create drop down components for the dynamic form.  
 * 
 * @author TVH Group NV 
 *
 * @param <T>
 */
public class DropDownPanel<T> extends DynamicFormComponentPanel {
	
	private static final long serialVersionUID = -4437270061698645543L;
	
	protected static final Logger logger = LoggerFactory.getLogger(DropDownPanel.class);
	
	/**
	 * Radio panel html markup constants
	 */
	private static final String DROP_DOWN_ID = "dropDown_id"; 
	private static final String DROP_DOWN_LABEL_ID = "dropDown_label";
	
	private DropDownChoice<T> dropDown;
	
	private String propertyName;
	
	public DropDownPanel(String id, IModel<T> model, final String propertyName, IModel<String> label, List<IFieldValue<T>> options) {
		super(id, model);
		
		this.propertyName = propertyName;
		
		add(new Label(DROP_DOWN_LABEL_ID, label));
		
		Map<T, IFieldValue<T>> optionsMap = new HashMap<T, IFieldValue<T>>(options.size());
		for (IFieldValue<T> fieldValue : options) {
			optionsMap.put(fieldValue.getValue(), fieldValue);
		}	
		
		dropDown = new DropDownChoice<T>(DROP_DOWN_ID, model, new ArrayList<T>(optionsMap.keySet()), new FieldValueOptionsRenderer<T>(optionsMap));
		dropDown.setOutputMarkupId(true);
		dropDown.setLabel(label);
		dropDown.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(dropDown);
	}
	
	public <S extends IDataBean> DropDownPanel(String id, IModel<T> model, final String propertyName, IModel<String> label, 
			IDataObject<S> linkedDataObject, String linkedFilterColumnName, String linkedDisplayColumn) {
		super(id, model);
		
		this.propertyName = propertyName;
		
		add(new Label(DROP_DOWN_LABEL_ID, label));
		
		Map<T, IFieldValue<T>> optionsMap = new HashMap<T, IFieldValue<T>>();
		
		linkedDataObject.resetDataBuffer();
		if (!linkedDataObject.getCursorState().equals(CursorStates.NoRecordAvailable)) {
			DataObjectIterator<S> linkedDataIterator = new DataObjectIterator<S>(linkedDataObject);
			while (linkedDataIterator.hasNext()) {
				T linkedValue =  (T)DataBeanUtil.getFieldValue(linkedDataIterator.next(), linkedFilterColumnName, null);
				String linkedValueLabel  =  DataBeanUtil.getFieldValue(linkedDataObject.getData(), linkedDisplayColumn, null).toString();
				
				optionsMap.put(linkedValue, new FieldValue<T>(linkedValueLabel, linkedValue, linkedValueLabel)); 
			}
		}
		
		dropDown = new DropDownChoice<T>(DROP_DOWN_ID, model, new ArrayList<T>(optionsMap.keySet()), new FieldValueOptionsRenderer<T>(optionsMap));
		dropDown.setOutputMarkupId(true);
		dropDown.setLabel(label);
		dropDown.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(dropDown);
	}
	
	
	public FormComponent<?> getFormComponent() {
		return dropDown;
	}

	public String getInputPropertyName() {
		return propertyName;
	}
	
	public void setFormComponentModelObject(IDataBean dataBean) {

		dropDown.setModel(new PropertyModel<T>(dataBean, propertyName));
	} 
}
