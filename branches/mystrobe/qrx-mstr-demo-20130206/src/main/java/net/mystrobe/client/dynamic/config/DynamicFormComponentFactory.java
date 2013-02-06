package net.mystrobe.client.dynamic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mystrobe.client.WicketDSRuntimeException;

public class DynamicFormComponentFactory {

	protected static final Logger logger = LoggerFactory.getLogger(DynamicFormComponentFactory.class);
			
	private static String FORM_COMPONENTS_FACTORY_CLASS = "FORM_COMPONENTS_FACTORY_CLASS";
	
	private static IDynamicFormComponentBuilder formComponentBuilderInstance = null;
	
	
	public static IDynamicFormComponentBuilder getBuilder() {
		
		if (formComponentBuilderInstance == null) {
			
			String className = System.getProperty(FORM_COMPONENTS_FACTORY_CLASS);
			
			if (className == null ) {
				className = DynamicFormComponentBuilder.class.getName();
			}
			
			try {
				formComponentBuilderInstance = (IDynamicFormComponentBuilder) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				logger.error("Can not instantiate form component builder class.", e);
				throw new WicketDSRuntimeException(e);
			} catch (IllegalAccessException e) {
				logger.error("Can not access component builder class.", e);
				throw new WicketDSRuntimeException(e);
			} catch (ClassNotFoundException e) {
				logger.error("Can not find component builder class.", e);
				throw new WicketDSRuntimeException(e);
			}
		}
		
		return formComponentBuilderInstance;
	}
}
