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
