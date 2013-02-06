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

import java.io.Serializable;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.page.ISelectRecordComponent;
import net.mystrobe.client.util.DataBeanUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Selectable text field form component.</br>
 * 
 * <p>
 * Displays a text field and a lookup arrow link next to it which
 *  opens a model popup with a list of possible values to select from.</p>
 *      
 * 
 * @author TVH Group NV
 *
 * @param <T> Current text field data type.
 * @param <S> Linked data object type.
 * @param <M> Selectable component panel type.  
 */
public class SelectableTextFieldPanel<T extends Serializable, S extends IDataBean, M extends ISelectRecordComponent> extends DynamicFormComponentPanel<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(SelectableTextFieldPanel.class); 
	
	private static final long serialVersionUID = -3401492928135987240L;
	
	private static final String TEXT_LABEL_ID = "textField_label"; 
	
	private static final String TEXT_DESCRIPTION_LABEL_ID = "textDescription_label";
	
	private static final String TEXT_FIELD_LOOK_UP_ID = "textField_lookUpLinkId";

	private static final String TEXT_FIELD_SELECT_RECORD_MODAL_WINDOW_ID = "selectRecordModalWindowId";
	
	private Label textLabel;
	
	private AjaxLink<T> lookupLink;
	
	private ModalWindow selectTextFieldValueWindow;
	
	private Class<M> selectRecordModalPanelClass;
	
	private M selectRecordModalPanel;

	private IDataObject<S> linkedDataObject;

	private String linkedColumnName;
	
	private IDataBean lastDataBean;
	
	private String visibleColumnName;
	
	private String displayText;
	
	private String modelObjectDescriptionField;
	
	private IDynamicFormConfig<S> selectRecordWindowTableConfig;
	
	/**
	 * Constructor
	 * 
	 * @param id Selectable text field form input panel id.
	 * @param model Text field data type model.
	 * @param propertyName Data object property/field/column name.
	 * @param labelModel Text field label.
	 * @param required Required flag.
	 * @param linkedDataObject Linked data object which will be used as data source in the popup window.  
	 * @param linkedDataObjectColumn Linked data object column. used to fetch value for form text field.
	 * @param selectRecordModalWindowPanelClass Modal window selectable panel type. Used to set the modal window contents.
	 */
	public SelectableTextFieldPanel(String id, IModel<T> model, String propertyName, IModel<String> labelModel, 
			boolean required, boolean readOnly, IDataObject<S> linkedDataObject, String linkedDataObjectColumn,
			Class<M> selectRecordModalWindowPanelClass, String visibleColumnName, String modelObjectDescriptionField, IDynamicFormConfig<S> selectRecordWindowTableConfig) {
		
		super(id, model, propertyName, required, readOnly);
		
		if( selectRecordModalWindowPanelClass == null ) (new IllegalStateException("null panel class")).printStackTrace();
		
		this.selectRecordModalPanelClass = selectRecordModalWindowPanelClass;
		this.selectRecordWindowTableConfig = selectRecordWindowTableConfig; 
		this.linkedColumnName = linkedDataObjectColumn;
		this.linkedDataObject = linkedDataObject;
		this.visibleColumnName = visibleColumnName;
		this.modelObjectDescriptionField = modelObjectDescriptionField;
		 
		this.displayText = (model.getObject() != null ? model.getObject().toString() : "");
		
		textLabel = new Label(TEXT_DESCRIPTION_LABEL_ID, new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				return (SelectableTextFieldPanel.this.getModelObject() != null && displayText != null) ? displayText : "" ;
			}
		});
		textLabel.setOutputMarkupId(true);
		textLabel.add(FIELD_NOT_VALID_BEHAVIOR);
		add(textLabel);
		
		setRequired(this.required);
		setLabel(labelModel);
		
		FormComponentLabel label = new DynamicFormComponentLabel(TEXT_LABEL_ID, SelectableTextFieldPanel.this, required);
        label.setDefaultModel(labelModel);
		add(label);
		
		selectTextFieldValueWindow = new ModalWindow(TEXT_FIELD_SELECT_RECORD_MODAL_WINDOW_ID, model);
		selectTextFieldValueWindow.setInitialWidth(800);
        
		add(selectTextFieldValueWindow);
		
		selectTextFieldValueWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            private static final long serialVersionUID = -6131202734588658455L;

			@SuppressWarnings("unchecked")
			public void onClose(AjaxRequestTarget target)
            {
				if (selectRecordModalPanel != null 
						&& selectRecordModalPanel.isSelected() 
						&& selectRecordModalPanel.getSelectedData() != null) {
					
					T selectedObject =  (T) DataBeanUtil.getFieldValue(selectRecordModalPanel.getSelectedData(),
							SelectableTextFieldPanel.this.linkedColumnName, null);
					
					getModel().setObject(selectedObject);
					lookupLink.setModelObject(selectedObject);
					
					Object displayObject = null;
					
					if (SelectableTextFieldPanel.this.visibleColumnName != null ) {
						displayObject = DataBeanUtil.getFieldValue(selectRecordModalPanel.getSelectedData(),
								SelectableTextFieldPanel.this.visibleColumnName, null);
					} else {
						displayObject = selectedObject;
					}
					
					displayText = displayObject != null ? displayObject.toString() : "";
					
					target.add(textLabel);
				}
			}
        });
        
		lookupLink =  new AjaxLink<T>(TEXT_FIELD_LOOK_UP_ID, model) {
			
			private static final long serialVersionUID = -2204510354326558489L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				if( selectRecordModalPanel == null ) {

					try {										
						Class<?> [] constructorArgumentTypes;
						Object [] constructorArgumentTypeInstances;
						if (SelectableTextFieldPanel.this.linkedDataObject != null ) {
							
							if (SelectableTextFieldPanel.this.selectRecordWindowTableConfig != null) {
								constructorArgumentTypes = new Class [] {String.class, ModalWindow.class, IDataObject.class, IDynamicFormConfig.class};
								constructorArgumentTypeInstances = new Object [] {selectTextFieldValueWindow.getContentId(),
										selectTextFieldValueWindow, SelectableTextFieldPanel.this.linkedDataObject, 
										SelectableTextFieldPanel.this.selectRecordWindowTableConfig};
							} else {
								constructorArgumentTypes = new Class [] {String.class, ModalWindow.class, IDataObject.class};
								constructorArgumentTypeInstances = new Object [] {selectTextFieldValueWindow.getContentId(),
										selectTextFieldValueWindow, SelectableTextFieldPanel.this.linkedDataObject};
							}
							
						} else {
							constructorArgumentTypes = new Class [] {String.class, ModalWindow.class};
							constructorArgumentTypeInstances = new Object [] {selectTextFieldValueWindow.getContentId(), selectTextFieldValueWindow};
						}
						
						selectRecordModalPanel = selectRecordModalPanelClass.getConstructor(constructorArgumentTypes)
							.newInstance(constructorArgumentTypeInstances);
						
						selectRecordModalPanel.initializePageComponents();
						
					} catch (Exception e) {					
						logger.error("Can not instantiare select record modal panel class:" + selectRecordModalPanelClass.toString(), e);
					} 			
					if( selectRecordModalPanel instanceof ModalWindow.PageCreator ) {
						selectTextFieldValueWindow.setPageCreator( (ModalWindow.PageCreator) selectRecordModalPanel);
					} else {
						selectTextFieldValueWindow.setContent((Component)selectRecordModalPanel);
					}
					
				}
				
				if( selectRecordModalPanel != null && selectRecordModalPanel instanceof ISelectRecordComponent ) {
					((ISelectRecordComponent) selectRecordModalPanel).setSelectedData(lastDataBean);
				}
				selectTextFieldValueWindow.show(target);
			}
		};
		lookupLink.setOutputMarkupId(true);
		
		add(lookupLink);
	}
	
	public String getInputPropertyName() {
		return propertyName;
	}
	
	@Override
	public void disableFormFieldPanel() {
		lookupLink.setEnabled(false);
	}

	
	public void enableFormFieldPanel() {
		if (!readOnly) {
			lookupLink.setEnabled(true);
		}
	}

	public FormComponent<T> getFormComponent() {
		return this;
	}
	
	public void setFormComponentModelObject(IDataBean dataBean) {
		super.setFormComponentModelObject(dataBean);
		this.lastDataBean = dataBean;
		
		IModel<T> newModel = PropertyModel.<T>of(dataBean, this.propertyName);
		lookupLink.setDefaultModel(newModel);
		
		Object displayObject = null;
		
		if (modelObjectDescriptionField != null) {
			displayObject = DataBeanUtil.getFieldValue(dataBean, modelObjectDescriptionField, null);
		} else {
			displayObject = newModel.getObject();
		}
		
		displayText = displayObject != null ? displayObject.toString() : "";
	}
	
	@Override
	protected void convertInput() {
		if (lookupLink.getModelObject() != null) {
			T input = lookupLink.getModelObject();
			setConvertedInput(input);
		} else {
			setModelObject(null);
		}
	}
}
