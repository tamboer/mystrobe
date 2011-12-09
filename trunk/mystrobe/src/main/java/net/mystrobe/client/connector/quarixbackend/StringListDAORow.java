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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.SchemaColumnProperties;
import net.mystrobe.client.connector.LocalizationProperties;
import net.mystrobe.client.impl.DAORow;
import net.mystrobe.client.util.DataBeanUtil;

import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
public class StringListDAORow<T extends IDataBean> extends DAORow<T> implements Serializable {

   	private static final long serialVersionUID = 5331777403082303368L;
	
	protected List<String> row;
    protected IDAOSchema<T> schema;
    protected Class<T> dataTypeClass;
    protected LocalizationProperties localizationProperties;

    public StringListDAORow(IDAOSchema<T> schema, Class<T> dataTypeClass, List<String> row, LocalizationProperties localizationProperties) {

        if( schema == null ||  dataTypeClass == null || row == null ) throw new IllegalArgumentException("<schema>, <dataTypeClass> or <row> is null");

        this.schema = schema;
        this.dataTypeClass = dataTypeClass;
        this.row = row;
        this.localizationProperties = localizationProperties;
    }

    
    @Override
    public T getRowData() {
        if( this.rowData != null ) return this.rowData;

        try {
            this.rowData =  dataTypeClass.newInstance();

            Iterator<String> rowIterator = row.iterator();
            Collection<String> columns = schema.getColumnNames();
            String format = null;
            String value = null;
            for (String column : columns) {
                if(rowIterator.hasNext()) {
                    value =  rowIterator.next();
                    if( schema.getColumnProperties(column).containsKey(SchemaColumnProperties.Format) ) {
                        format = schema.getColumnProperties(column).get(SchemaColumnProperties.Format);
                    } else {
                        format = null;
                    }

                    try {
                        Field field = this.rowData.getClass().getDeclaredField( NamingHelper.getFieldName(column));
                        DataBeanUtil.setFieldValue(field, this.rowData, value, format, localizationProperties);
                    } catch (SecurityException e) { throw new RuntimeException(e);
                    } catch (NoSuchFieldException e) { throw new RuntimeException(e);
                    }
                }
                else {
                    LoggerFactory.getLogger(StringListDAORow.class.getName()).error( "No JSON value found for: "+ column);
                }
            }
            
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if( this.beforeImage == null ) {

            try {
                this.beforeImage = dataTypeClass.newInstance();

                Iterator<String> rowIterator = row.iterator();
                Collection<String> columns = schema.getColumnNames();
                String format = null;
                String value = null;
                for (String column : columns) {
                    if(rowIterator.hasNext()) {
                        value = rowIterator.next();
                        if( schema.getColumnProperties(column).containsKey(SchemaColumnProperties.Format) ) {
                            format = schema.getColumnProperties(column).get(SchemaColumnProperties.Format);
                        } else {
                            format = null;
                        }
                        
                        try {
                            Field field = this.beforeImage.getClass().getDeclaredField( NamingHelper.getFieldName(column));
                            DataBeanUtil.setFieldValue(field, this.beforeImage, value, format, localizationProperties);
                        } catch (SecurityException e) { throw new RuntimeException(e);
                        } catch (NoSuchFieldException e) { throw new RuntimeException(e);
                        }
                    }
                    else {
                        LoggerFactory.getLogger(StringListDAORow.class.getName()).error( "No JSON value found for: "+ column);
                    }
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }            
        }
        
        return this.rowData;
    }

    @Override
	public String toString() {
    	
    	StringBuilder stringBuilder = new StringBuilder(super.toString());
		
    	if (this.rowData != null ) {
    		stringBuilder.append(DataBeanUtil.toString(this.rowData));
    	} else if (this.row != null) {
    		stringBuilder.append(this.row).append("\n");
		} 
		
		return stringBuilder.toString();
	}
}
