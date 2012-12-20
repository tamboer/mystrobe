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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.mystrobe.client.ComponentLinker;
import net.mystrobe.client.CursorStates;
import net.mystrobe.client.DataLinkParameters;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataListener;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.IDataSource;
import net.mystrobe.client.IStateListener;
import net.mystrobe.client.IStateSource;
import net.mystrobe.client.IUpdateListener;
import net.mystrobe.client.IUpdateSource;
import net.mystrobe.client.IUpdateUIActionListener;
import net.mystrobe.client.IUpdateUIActionSource;
import net.mystrobe.client.UpdateStates;
import net.mystrobe.client.connector.quarixbackend.NamingHelper;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig;
import net.mystrobe.client.dynamic.config.IFieldValue;
import net.mystrobe.client.dynamic.page.AbstractSelectRecordModalPanel;
import net.mystrobe.client.dynamic.page.SelectRecordModalPanel;
import net.mystrobe.client.error.DefaultErrorHandler;
import net.mystrobe.client.util.StringUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Dynamic data object form component.<br/>
 * 
 * <p>
 * Creates a form input for a data object using {@link IDynamicFormConfig} for configuration options.  
 * Separate input types can be used for different data object fields.</p>
 * 
 * Implements {@IDataListener} to receive form data and has to be linked to a {@link IDataSource}.
 * Update actions are triggered by a {@link IUpdateUIActionSource}(usually a CRUD operations panel) component and received   
 *  by the form component ({@link IUpdateUIActionListener})  merely for validation purpose, error messages handling.<br/>
 *  
 * Dynamic form also acts as a {@link IUpdateSource} for the data object({@link IUpdateListener}).<br/>
 * 
 * In order for the component to work properly all above linkings
 *  have to be set using the {@link ComponentLinker} between form component and other
 *  components acting as sources/listener of each relation. Additional methods have to be 
 *  overridden so that ajax requests refresh all components in the page.</p>   
 * 
 * @author TVH Group NV
 *
 * @param <T> Data object type parameter.
 */
public class DynamicFormDataViewPanel<T extends IDataBean> extends Panel implements IUpdateUIActionListener, IDataListener<T>, IUpdateSource<T> {

	private static final long serialVersionUID = 8193034855133952807L;
	
	private static final Logger logger = LoggerFactory.getLogger(DynamicFormDataViewPanel.class);

	/**
	 * Dynamic form html markup constants
	 */
	private static final String FORM_INPUT_PANEL_ID = "formInput_PanelId";
	private static final String DYNAMIC_HTML_BEFORE_INPUT_PANEL = "dynamicHtmlBeforeInputPanel";
	private static final String DYNAMIC_HTML_AFTER_INPUT_PANEL = "dynamicHtmlAfterInputPanel";
	
	protected final static String TABLE_ROW_START_TAG = "<tr>";
	protected final static String TABLE_ROW_END_TAG = "</tr>";
	
	protected IUpdateListener<T> updateListener;
	protected IDataSource<T> dataSource;
	protected List<IStateListener> stateListeners = new ArrayList<IStateListener>();
	
	private UpdateStates updateState = UpdateStates.UpdateComplete;
    private CursorStates cursorState = CursorStates.NoRecordAvailable;
	protected int formFieldsOnRow = 1; 
	
	protected List<DynamicFormComponentPanel> formFields;
	
	public DynamicFormDataViewPanel(String id, T dataBean, IDynamicFormConfig<T> formConfig) {
		super(id);
		
		setOutputMarkupId(true);
		//add external css file reference
		//add(CSSPackageResource.getHeaderContribution(DynamicFormDataViewPanel.class, "dynamicForm.css"));
		
		if (formConfig != null) {
			formFieldsOnRow = formConfig.getNumberOfFieldsOnRow();
		}
		
		formFields = buildFormInputFields(formConfig, dataBean);
		
		ListView<DynamicFormComponentPanel> panelInputFields = new ListView<DynamicFormComponentPanel>("formInputFields_Id", formFields) {

			private static final long serialVersionUID = -3290979835468097717L;

			@Override
			protected void populateItem(ListItem<DynamicFormComponentPanel> item) {
				
				int inputFieldPosition = item.getIndex()  + 1;
				
				String dynamicHtmlBeforeInputPanel = ""; 
				if (formFieldsOnRow == 1 || (inputFieldPosition % formFieldsOnRow == 1)) {
					dynamicHtmlBeforeInputPanel = TABLE_ROW_START_TAG;
				}
				
				Label tableRowBefore = new Label(DYNAMIC_HTML_BEFORE_INPUT_PANEL, dynamicHtmlBeforeInputPanel);
				tableRowBefore.setRenderBodyOnly(true);
				tableRowBefore.setEscapeModelStrings(false);
				item.add(tableRowBefore);
				
				String dynamicHtmlAfterInputPanel = ""; 
				if (formFieldsOnRow == 1 || (inputFieldPosition % formFieldsOnRow == 0) 
						|| inputFieldPosition == getViewSize()) {
					dynamicHtmlAfterInputPanel = TABLE_ROW_END_TAG;
				} 
				
				//when last table row td count is less than formFieldsOnRow add td with appropriate colspan 
				if (inputFieldPosition == getViewSize() && (inputFieldPosition % formFieldsOnRow > 0)) {
					int colspan = (formFieldsOnRow - (inputFieldPosition % formFieldsOnRow)) * 2;
					dynamicHtmlAfterInputPanel = "<td colspan='" + colspan + "'/>" + dynamicHtmlAfterInputPanel;
				}
				
				Label tableRowAfter = new Label(DYNAMIC_HTML_AFTER_INPUT_PANEL, dynamicHtmlAfterInputPanel);
				tableRowAfter.setRenderBodyOnly(true);
				tableRowAfter.setEscapeModelStrings(false);
				item.add(tableRowAfter);
				
				item.add(item.getModelObject());
			}
		};
		
		panelInputFields.setRenderBodyOnly(true);
		
		add(panelInputFields);
	}
	
	/**
	 * Enable form input fields and buttons for editing.
	 */
	public void enableFormInputFields () {
		for (DynamicFormComponentPanel fieldPanel : formFields) {
			fieldPanel.enableFormFieldPanel();
		}
	}
	
	/**
	 * Disable form input fields and buttons for editing.
	 */
	public void disableFormInputFields () {
		for (DynamicFormComponentPanel fieldPanel : formFields) {
			fieldPanel.disableFormFieldPanel();
		}
	}
	
	/**
	 * Change model object
	 */
	public void updateFormModelObject(IDataBean modelInstance) {
		for (DynamicFormComponentPanel fieldPanel : formFields) {
			fieldPanel.setFormComponentModelObject(modelInstance);
		}
	}
	
	
	/**
	 * Create form input panel using type from data base schema. 
	 * 
	 * @param dataBean Data object.
	 * @param config Configuration info.
	 * 
	 * @return List of form input fields panels
	 */
	private List<DynamicFormComponentPanel>  buildFormInputFields(IDynamicFormConfig<T> config, T dataBean) {
		
		List<DynamicFormComponentPanel> formInputFields = new ArrayList<DynamicFormComponentPanel>();
		
		Collection<String> columnNames = config.getVisibleColumnNames();
				
		for (String columnName : columnNames) {
		
			if ( IDataBean.ROW_ID_FIELD_NAME.equalsIgnoreCase(columnName) || 
					IDataBean.ROW_STATE_FIELD_NAME.equals(columnName)  ) {
				continue;
			}
			
			Map<IDynamicFormFieldConfig.Property, Object> configurationMap =  config.getColumnProperties(columnName);
			
			DynamicFormComponentPanel formFieldPanel = buildFormFieldPanel(columnName, configurationMap, dataBean, config.getLocalizableFormLabels());
			
			if (formFieldPanel != null) {
				formFieldPanel.disableFormFieldPanel();
				formFieldPanel.setOutputMarkupId(true);
				formInputFields.add(formFieldPanel);
			}
		}
		
		return formInputFields;
	}
	
	/**
	 * Build form filed input panel from db schema and configuration.<br/>
	 * 
	 * Configuration properties if available override any schema settings  
	 * 
	 * @param columnName Field db column name.
	 * @param propertiesMap Db schema properties map.
	 * @param configurationMap Configuration properties map. 
	 * @param dataBean Data object.
	 * 
	 * @return New form input panel according to schema and config.
	 */
	private <S> DynamicFormComponentPanel buildFormFieldPanel(String columnName, Map<IDynamicFormFieldConfig.Property, Object> configurationMap, T dataBean, boolean localizabeLabels) {
		
		IModel<String> columnLabel = null;
		Object formFieldLabel = configurationMap.get(IDynamicFormFieldConfig.Property.Label);
		
		if (formFieldLabel instanceof IModel<?> ) {
			columnLabel = (IModel<String>) formFieldLabel;
		} else {
			String label = (String) formFieldLabel;
			
			if (localizabeLabels) {
				columnLabel = new ResourceModel(label, StringUtil.buildDefaultResourceValue(label));
			} else {
				if (StringUtil.isNullOrEmpty(label)) {
					label = columnName;
				}
				columnLabel = Model.of(label);
			}
		}
		
		boolean required =  configurationMap.containsKey(IDynamicFormFieldConfig.Property.Required) && 
				(Boolean) configurationMap.get(IDynamicFormFieldConfig.Property.Required);
		
		boolean readOnly = configurationMap.containsKey(IDynamicFormFieldConfig.Property.ReadOnly) && 
			(Boolean) configurationMap.get(IDynamicFormFieldConfig.Property.ReadOnly);
		
		String format =  (String) configurationMap.get(IDynamicFormFieldConfig.Property.Format);
		List<IFieldValue<S>> options = (List<IFieldValue<S>>) configurationMap.get(IDynamicFormFieldConfig.Property.ValuesList);
		
		if (!configurationMap.containsKey(IDynamicFormFieldConfig.Property.Type)) {
			throw new IllegalArgumentException("Dynamic form field configuration doesn't have type set for field name:" + columnName);
		}
		
		IDynamicFormFieldConfig.FieldType fieldType = (IDynamicFormFieldConfig.FieldType) configurationMap.get(IDynamicFormFieldConfig.Property.Type);
		
		return buildFormPanelFromConfigType(columnName, fieldType, dataBean, columnLabel, options, required, readOnly, format, configurationMap);
	}
	
	/**
	 * Buld form field panel using type from configuration.
	 * @param <S>
	 * 
	 * @param columnName Field db column name
	 * @param fieldType Field type set in configuration.
	 * @param dataBean Data bean
	 * @param columnLabel Field label
	 * @param required Required flag.
	 * 
	 * @return Form input field panel.
	 */
	private <S, M extends IDataBean> DynamicFormComponentPanel buildFormPanelFromConfigType(String columnName, IDynamicFormFieldConfig.FieldType fieldType, T dataBean,
					IModel<String> columnLabel, List<IFieldValue<S>> options,  boolean required, boolean readOnly, String format, Map<IDynamicFormFieldConfig.Property, Object> configurationMap) {
		
		DynamicFormComponentPanel result = null;
		PropertyModel<S> propertyModel; 
		String normalizedColumnName = NamingHelper.getFieldName(columnName);
		switch (fieldType) {
			case CheckBox: 
				PropertyModel<Boolean> model = new PropertyModel<Boolean>(dataBean, normalizedColumnName);
				result = new CheckBoxPanel(FORM_INPUT_PANEL_ID, model, normalizedColumnName, columnLabel, required, readOnly);
				break;
				
			case TextField :
				propertyModel = new PropertyModel<S>(dataBean, normalizedColumnName);
				Boolean selectableFieldValue = (Boolean) configurationMap.get(IDynamicFormFieldConfig.Property.SelectableFieldValue);
				
				if (selectableFieldValue != null && selectableFieldValue) {
					IDataObject<M> linkedDataSource = (IDataObject<M>) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedDataObject);
					String linkedColumnName  = (String) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedColumnName);
					
					Class<AbstractSelectRecordModalPanel<M>> selecatbleRecordModalPanelClass  = (Class<AbstractSelectRecordModalPanel<M>>) configurationMap.get(IDynamicFormFieldConfig.Property.SelectableModalWindowPanelClass);
					
					if( selecatbleRecordModalPanelClass == null ) {
						selecatbleRecordModalPanelClass = (Class) SelectRecordModalPanel.class; 
					}
					
					result = new SelectableTextFieldPanel<S, M, AbstractSelectRecordModalPanel<M>>(FORM_INPUT_PANEL_ID,  propertyModel, normalizedColumnName, columnLabel, required, readOnly, linkedDataSource, linkedColumnName, selecatbleRecordModalPanelClass);
				} else {
					result = new TextFieldPanel<S>(FORM_INPUT_PANEL_ID, propertyModel, normalizedColumnName, columnLabel, required, readOnly, 0);
				}
				
				break;
				
			case DateField :
				PropertyModel<Date> dateModel = new PropertyModel<Date>(dataBean, normalizedColumnName);
				result = new DateFieldPanel(FORM_INPUT_PANEL_ID, dateModel, normalizedColumnName, columnLabel, required, readOnly, format);
				break;	
				
			case TextArea :
				propertyModel = new PropertyModel<S>(dataBean, normalizedColumnName);
				result = new TextAreaPanel<S>(FORM_INPUT_PANEL_ID, propertyModel, normalizedColumnName, columnLabel, required, readOnly, 0);
				break;
				
			case Radio :
				propertyModel = new PropertyModel<S>(dataBean, normalizedColumnName);
				result = new RadioPanel<S>(FORM_INPUT_PANEL_ID, propertyModel, normalizedColumnName, columnLabel, options, false, required, readOnly);
				break;
				
			case DropDown :
				propertyModel = new PropertyModel<S>(dataBean, normalizedColumnName);
				result = new DropDownPanel<S>(FORM_INPUT_PANEL_ID, propertyModel, normalizedColumnName, columnLabel, options, required, readOnly);
				break;
				
			case AutoCompleteTextField :
				propertyModel = new PropertyModel<S>(dataBean, normalizedColumnName);
				IDataObject<M> autoCompleteDataSource = (IDataObject<M>) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedDataObject);
				String autoCompleteFilterColumnName  = (String) configurationMap.get(IDynamicFormFieldConfig.Property.LinkedColumnName);
				result = new AutoCompleteTextFieldPanel<S,M>(FORM_INPUT_PANEL_ID, propertyModel, normalizedColumnName, columnLabel, 
						required, readOnly, autoCompleteDataSource, autoCompleteFilterColumnName);
				break;
		}
		
		return result;
	}
	
	public void addStateListener(IStateListener statelistener) {
		stateListeners.add(statelistener);
	}

	public void addRecord() {
        if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");
        this.updateListener.createData(false);
        this.updateMode(UpdateStates.UpdateBegin);		
	}
	
	public void addRecord(Map<String, Object> initValuesMap) {
        if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");
        this.updateListener.createData(false);
        this.updateListener.setDataInitialValues(initValuesMap);
        this.updateMode(UpdateStates.UpdateBegin);		
	}
    

	public void cancelRecord() {
		if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");
        
		this.updateListener.cancelCRUDOpertaion() ;
        this.updateMode(UpdateStates.UpdateComplete);
    }

	public void copyRecord() {
		if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");

		this.updateListener.createData(true);
        this.updateMode(UpdateStates.UpdateBegin);		
	}


	public void deleteRecord() {
        if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");
        try {
			updateListener.deleteData();
		} catch (WicketDSBLException e) {
			logger.error("BL error while deleting data:" + e);
		}
    }

	public void resetRecord() {
		if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");
        updateListener.resetData();
	}
	
	public void resetRecord(Map<String, Object> initValuesMap) {
		if( this.updateListener == null ) throw new IllegalStateException("Unable to add record because dataview does not have a update listener set");
		this.updateListener.resetData();
        this.updateListener.setDataInitialValues(initValuesMap);
	}

	public void saveRecord() {
		try {
			getUpdateListener().updateData();
		} catch (WicketDSBLException e) {
			logger.error("BL error while updating data:" + e);
		}
		
		if (this.updateListener.getUpdateDateCommitSuccess()) {
		 	updateMode(UpdateStates.UpdateComplete);
		}
	}
	
	public void editRecord() {
		updateMode(UpdateStates.UpdateBegin);
	}

	public UpdateStates getUpdateState() {
		return updateState;
	}

	public void updateMode(UpdateStates updateState) {
        logger.info("DataView updateMode : " + updateState.name());

        switch ( updateState ) {

            case Update:
            	enableFormInputFields();
                updateState(this, updateState);
                break;


            case UpdateBegin:
            	enableFormInputFields();
                updateState(this, updateState);
                break;


            case UpdateComplete:
            case UpdateEnd:
                disableFormInputFields();
                updateState(this, updateState);
                break;

        }
		this.updateState = updateState;
	}

	public IUpdateListener<T> getUpdateListener() {
		return updateListener;
	}

	public void setUpdateListener(IUpdateListener<T> updateListener) {
		this.updateListener = updateListener;
	}

	public CursorStates getCursorState() {
		return cursorState;
	}

	public Collection<IStateListener> getStateListeners() {
		return stateListeners;
	}

	public void removeStateListener(IStateListener listener) {
		stateListeners.remove(listener);
	}

	public void cursorState(IStateSource source, CursorStates cursorState) {
//		CursorStates oldCursorStates = this.cursorState;
    }

	public void updateState(IStateSource source, UpdateStates updateState) {
		this.updateState = updateState;
		
		for (IStateListener stateListener : stateListeners ) {
			stateListener.updateState(source, updateState);
		}
	}

	public void dataAvailable(IDataBean modelInstance, CursorStates cursorState) {
		logger.debug("DynamicFormDataViewPanel received data with row id " + modelInstance.getRowId() 
				+ " class " + modelInstance.getClass().getName() + " from data object " + getDataSource().hashCode());
		updateFormModelObject(modelInstance);
	}
	
	public IDataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(IDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Set<DataLinkParameters> getDataLinkParameters() {
		return null;
	}

	public void addDataLinkParameter(DataLinkParameters dataLinkParameter) {
	}
	
	public void setDataLinkParameters(Set<DataLinkParameters> dataLinkParametersSet) {
	}
	
    public void clearDataLinkParameters() {
    }

	@Override
	public void onUpdateError() {
		DefaultErrorHandler.handleErrors(this.getUpdateListener(), this, formFields);
	}
	
	
}
