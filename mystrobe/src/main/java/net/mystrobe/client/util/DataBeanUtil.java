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
 package net.mystrobe.client.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.progress.common.util.Base64;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.LocalizationProperties;
import net.mystrobe.client.connector.quarixbackend.NamingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TVH Group NV
 */
public class DataBeanUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DataBeanUtil.class);
	
	
	public static void setFieldValue(Field field, Object instance, Object obj, String format, LocalizationProperties localizationProperties) {
		Object value = obj;
        String strValue = (String) obj;
		Class<?> type = field.getType();
		
		if( strValue == null || (strValue != null && strValue.trim().equals("?")) ) {
            value = null;
        } else if(type.equals(String.class)) {
            value = strValue;
        } else if(type.equals(int.class) || (type.equals(Integer.class))) {
            value = StringToJavaNativeUtil.parseInt(strValue);
		} else if(type.equals(long.class) || type.equals(BigDecimal.class)) {
			value = StringToJavaNativeUtil.parseDouble(strValue, localizationProperties.getNumericalDecimalPoint(), localizationProperties.getNumericalSeparator());
		} else if(type.equals(float.class)) {
                    value = StringToJavaNativeUtil.parseFloat(strValue, localizationProperties.getNumericalDecimalPoint());
		} else if(type.equals(double.class)) {
                    value = StringToJavaNativeUtil.parseDouble(strValue, localizationProperties.getNumericalDecimalPoint(), localizationProperties.getNumericalSeparator());
		} else if(type.equals(boolean.class) || (type.equals(Boolean.class))) {
                    if( strValue != null
                            && ( strValue.trim().toLowerCase().equals("true") || strValue.trim().toLowerCase().equals("yes"))) {
                        value = true;
                    } else value = false;
		} else if(type.equals(Date.class)) {
                    value = StringToJavaNativeUtil.parseDate(strValue, format,  localizationProperties.getDateFormat(), localizationProperties.getNumericalDecimalPoint());
		} else if (type.isArray()&& type.getComponentType().isPrimitive()
				&& type.getComponentType().equals(byte.class) ) {
			value = Base64.decode(strValue);
		}
        
        setFieldValue(field, instance, value);
	}
	
	/**
	 * Set object <tt>instance</tt> field value to <tt>value</tt>.
	 * 
	 * If field is not accessible method changes access rights so that
	 *  value can be changed and resets rights as they were beforew method call. 
	 *  
	 * @param field Field value to set.
	 * @param instance Object.
	 * @param value Value to set.
	 */
	private static void setFieldValue(Field field, Object instance, Object value) {
		try {
			
			if( value != null) {
				field.set(instance, value);
			}
		
		} catch (IllegalAccessException e) {
			Field[] fields = new Field[] { field };
			AccessibleObject.setAccessible(fields, true);
			try {
				field.set(instance, value);
			} catch (IllegalAccessException ignored) {
			}
			AccessibleObject.setAccessible(fields, false);
		}
	}
    
	/**
	 * Get object <tt>instance field</tt> value.
	 * 
	 * If field is not accessible method changes access rights so that
	 *  value can be changed and resets rights as they were beforew method call. 
	 *  
	 * @param field Field value to get.
	 * @param instance Object to read field value from.
	 */
	public static Object getFieldValue(Field field, Object instance) {
    	Object value = null;
    	try {
    		value =  field.get(instance);
		} catch (IllegalAccessException e) {
			Field[] fields = new Field[] { field };
			AccessibleObject.setAccessible(fields, true);
			try {
				value =  field.get(instance);
			} catch (IllegalAccessException ignored) {
			}
			AccessibleObject.setAccessible(fields, false);
		}
		return value;
    }
	
	public static <T extends IDataBean> Object getFieldValue(T instance, String fieldColumnName, final Class<? super T> fieldClass) {
		
		Object value = null;
		String propertyFieldName = NamingHelper.getFieldName(fieldColumnName);
		Class checkFieldClass = fieldClass;
		try {
			try {
			
				Field propertyField;
				if (checkFieldClass == null) {
					checkFieldClass = instance.getClass();
				}
				
				propertyField = checkFieldClass.getDeclaredField(propertyFieldName);
				value = getFieldValue(propertyField, instance);
			
			} catch (NoSuchFieldException e) {
				if (checkFieldClass.getSuperclass() != null) {
					logger.warn("Can not find field with name:" + propertyFieldName + " in data bean class " + checkFieldClass);
					return getFieldValue(instance, fieldColumnName, checkFieldClass.getSuperclass()); 
				} else {
					logger.error("Can not find field with name:" + propertyFieldName + " in data bean class " + checkFieldClass);
				}
			}
		} catch (SecurityException e) {
			logger.error("Can not access field with name:" + propertyFieldName + " in data bean class " + instance.getClass());
		} 
		
		return value;
	}
    
    /**
     * Copy data 
     * 
     * @param fromInstance
     * @param toInstance
     */
    public static void copyData(IDataBean fromInstance, IDataBean toInstance, boolean copyRowInfo, Class dataBeanClass) {
    	
    	Field[] fromInstanceFields = null;
    	Class classToCopyFieldsFor = dataBeanClass;  
    	
    	if (classToCopyFieldsFor == null ) {
    		classToCopyFieldsFor = fromInstance.getClass();
    	}
    	
    	fromInstanceFields = classToCopyFieldsFor.getDeclaredFields();
    	
    	for (Field fromDeclaredField : fromInstanceFields) {
    		
    		if (!copyRowInfo && (IDataBean.ROW_ID_FIELD_NAME.equals(fromDeclaredField.getName()) ||
    				IDataBean.ROW_STATE_FIELD_NAME.equals(fromDeclaredField.getName()))) {
    			continue;
    		}
    		
    		try {
    			Field toDeclaredField = classToCopyFieldsFor.getDeclaredField(fromDeclaredField.getName());
    			Object fieldValue = getFieldValue(fromDeclaredField, fromInstance);
				
				if (fieldValue != null) {
					setFieldValue(toDeclaredField, toInstance, fieldValue);
				}
				
    		} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			}
		}
    	
    	if (classToCopyFieldsFor.getSuperclass() != null) {
    		//copy data for superclass
    		copyData(fromInstance, toInstance, copyRowInfo, classToCopyFieldsFor.getSuperclass());
    	}
    }
    
    /**
     * Copy list of data to a new list.
     * 
     * @param listToCopy List to be cloned.
     * @param dataBeanClass Data beans class; 
     * @throws IllegalAccessException Can not instantiate data beans.
     * @throws InstantiationException Can not instantiate data beans.
     */
    public static <T extends IDataBean> List<T> copyDataList(List<IDAORow<T>> listToCopy, Class<T> dataBeanClass) throws InstantiationException, IllegalAccessException {
    	
    	List<T> result = new ArrayList<T>(listToCopy.size());
    	
    	for ( IDAORow<T> objectToCopy : listToCopy) {
    		T newInstance = dataBeanClass.newInstance();
    		copyData(objectToCopy.getRowData(), newInstance, true, null);
    		result.add(newInstance);
    	}
    	
    	return result;
    }
    
    /**
     * Data bean string info.
     * 
     * @param dataBeanInstance Bean.
     * @return String of all bean fields and values.
     */
    public static String toString(IDataBean dataBeanInstance) {
    	Field[] fromInstanceFields = dataBeanInstance.getClass().getDeclaredFields();
    	StringBuilder result = new StringBuilder(dataBeanInstance.getClass().getSimpleName());
    	result.append(" [ ");
    	
    	for (Field declaredField : fromInstanceFields) {
    		
    		try {
    			
    			Object fieldValue = getFieldValue(declaredField, dataBeanInstance);
				result.append(declaredField.getName()).append("=").append(fieldValue);
				result.append(" ; ");
				
    		} catch (SecurityException e) {
			} 
		}
    	
    	result.append(" ] ");
    	return result.toString();
    }
    
    /**
     * Populate bean fields with values taken from <tt>beanFieldValuesMap</tt> parameter.<br/> 
     * 
     * @param instance Bean instance to set values for.
     * @param beanFieldValuesMap Map of field names and values to set.
     */
    public static void setBeanFieldValues(IDataBean instance, Map<String, Object> beanFieldValuesMap ) {

    	if (beanFieldValuesMap == null || beanFieldValuesMap.isEmpty()) {
    		return;
    	}
    	
    	for (String fieldName : beanFieldValuesMap.keySet()) {
    		String normalizedFieldName = NamingHelper.getFieldName(fieldName);
    		try {
				setFieldValue(instance.getClass().getDeclaredField(normalizedFieldName), instance, beanFieldValuesMap.get(fieldName));
			} catch (SecurityException e) {
				logger.error("Can not set field value.", e);
			} catch (NoSuchFieldException e) {
				logger.error("Can not set field value.", e);
			}
    	}
    	
    }
	

}
