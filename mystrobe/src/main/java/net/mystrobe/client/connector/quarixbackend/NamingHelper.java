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
 package net.mystrobe.client.connector.quarixbackend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.SchemaColumnProperties;
import net.mystrobe.client.connector.LocalizationProperties;
import net.mystrobe.client.util.StringToJavaNativeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.common.util.Base64;


/**
 * @author TVH Group NV
 */
public class NamingHelper {
	
	protected static final Logger logger = LoggerFactory.getLogger(NamingHelper.class);
	
	private static ThreadLocal<Map<String, DecimalFormat>> decimalFormatsCacheMap = new ThreadLocal<Map<String, DecimalFormat>>();

    public static Map<String, DecimalFormat> getDecimalFormatsMap() {
    	
    	Map<String, DecimalFormat> result = decimalFormatsCacheMap.get();
    	
    	if (result == null) {
    		result = new HashMap<String, DecimalFormat>(4);
    		decimalFormatsCacheMap.set(result);
    	} 
    	return result;
    }
	
	public static String getFieldName( String columnName ) {
        if( columnName == null ) throw new IllegalArgumentException("argument [columnName] cannot be null");
        
        String field = "";

        for ( int i=0;  i < columnName.length(); i++) {
            if ( Character.isJavaIdentifierPart(columnName.charAt(i) ) ) field += columnName.charAt(i);
        }

        if( field.length() == 0 ) field = "column";
        return field;
    }


    public static String getSetterName( String fieldName ) {
        String camelField = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return camelField;
    }


    public static String getGetterName( String fieldName ) {
        String camelField = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return camelField;
    }


    public static String getFieldValue(Object instance, String column, IDAOSchema<?> schema, LocalizationProperties localizationProperties) {
        try {
            String fieldName = getFieldName(column);
            
            if( fieldName.equals("rowid") )
            	fieldName = "rowId";
            
            Method method = instance.getClass().getMethod(getGetterName(fieldName), new Class[0]);
            if (method != null) {
                Object value = method.invoke(instance, new Object[0]);
                
                if (value == null) {
                	Class<?> fieldType = instance.getClass().getDeclaredField(fieldName).getType();
					if (fieldType.equals(Integer.class)) {
                		return "?";
                	} else { 
                		//TODO: check for other types value to return for null value. Should match the in value. 
                		return "";
                	}
                }
                
                String dateFormat = null;
                if (value instanceof Date) {
                	if (schema != null) {
                		Map<SchemaColumnProperties, String> columnProperties =  schema.getColumnProperties(column);
                		
                		if (columnProperties.containsKey(SchemaColumnProperties.Format)) {
                			dateFormat = columnProperties.get(SchemaColumnProperties.Format);
                		}
                	}
                }
                
                return getFormattedOutputValue(value, dateFormat, localizationProperties);
            }

        } catch (NoSuchMethodException ex) {
            logger.error( null, ex);
        } catch (NoSuchFieldException ex) {
            logger.error( null, ex);
        } catch (SecurityException ex) {
        	logger.error( null, ex);
        } catch (IllegalAccessException ex) {
        	logger.error( null, ex);
        } catch (IllegalArgumentException ex) {
        	logger.error( null, ex);
        } catch (InvocationTargetException ex) {
        	logger.error( null, ex);
        }
        return null;
    }
    
    public static String getFormattedOutputValue(final Object value, final String serverDateFormat, final LocalizationProperties localizationProperties) {
    	
    	if ( value instanceof BigDecimal ) {
        	
        	String decimalFormatSymbolsKey = (new StringBuilder(localizationProperties.getNumericalSeparator())).append(localizationProperties.getNumericalDecimalPoint()).toString();
        	Map<String, DecimalFormat> decimalFormatsMap = getDecimalFormatsMap();
        	
        	DecimalFormat decimalFormat; 
        	
        	if (decimalFormatsMap.containsKey(decimalFormatSymbolsKey)) {
        		decimalFormat = decimalFormatsMap.get(decimalFormatSymbolsKey);
        	
        	} else {
        		
        		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            	symbols.setGroupingSeparator(localizationProperties.getNumericalSeparator());
            	symbols.setDecimalSeparator(localizationProperties.getNumericalDecimalPoint());
            	
            	decimalFormat = new DecimalFormat();
            	decimalFormat.setDecimalFormatSymbols(symbols);
            	
            	decimalFormatsMap.put(decimalFormatSymbolsKey, decimalFormat);
            }
        	return decimalFormat.format(((BigDecimal)value));
        
    	} else if (value instanceof Boolean) {
        	return (Boolean) value ? "yes" : "no";
        	
        } else if (value instanceof Date) {
        	
        	String formatDate;
        	if (value instanceof Timestamp) {
        		if (serverDateFormat == null) {
        			String timeZoneFormatKey = (new StringBuilder(localizationProperties.getDateFormat()).append(localizationProperties.getNumericalSeparator())).toString();
        			formatDate = StringToJavaNativeUtil.timeZoneFormatsMap.get(timeZoneFormatKey);
        		} else {
        			formatDate = serverDateFormat;
        		}
        	} else {
        		formatDate = serverDateFormat != null ? serverDateFormat : localizationProperties.getFormatDate();
        	}
        	
        	String result = StringToJavaNativeUtil.formatDate(((Date)value), formatDate, localizationProperties.getDateFormat(), localizationProperties.getNumericalSeparator());
        	
        	return result;
        
        } else if (value.getClass().isArray() &&  value.getClass().getComponentType().isPrimitive() &&
        		value.getClass().getComponentType().equals(byte.class)) {
        	return Base64.encode((byte [])value);
        }
        
        return String.valueOf(value);
    }

}
