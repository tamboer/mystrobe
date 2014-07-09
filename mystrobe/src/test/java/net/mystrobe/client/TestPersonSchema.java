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
 package net.mystrobe.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.SchemaColumnProperties;

/**
 * @author TVH Group NV
 */
public class TestPersonSchema implements IDAOSchema {

	
	private long batchSize = 0;
	
    protected static Map<String, Map> repository = new HashMap<String, Map>() {{

        put ("id", new HashMap<SchemaColumnProperties, String>() {{
            put( SchemaColumnProperties.DefaultValue, "-1");
            put( SchemaColumnProperties.Format, ">>>>>>>>9");
            put( SchemaColumnProperties.Type, "Integer");
        }});

        put ("firstName", new HashMap<SchemaColumnProperties, String>() {{
            put( SchemaColumnProperties.DefaultValue, "");
            put( SchemaColumnProperties.Format, "X(40)");
            put( SchemaColumnProperties.Type, "String");
        }});

        put ("lastName", new HashMap<SchemaColumnProperties, String>() {{
            put( SchemaColumnProperties.DefaultValue, "");
            put( SchemaColumnProperties.Format, "X(40)");
            put( SchemaColumnProperties.Type, "String");
        }});

       put ("language", new HashMap<SchemaColumnProperties, String>() {{
            put( SchemaColumnProperties.DefaultValue, "EN");
            put( SchemaColumnProperties.Format, "X(2)");
            put( SchemaColumnProperties.Type, "String");
        }});

       put ("countryCode", new HashMap<SchemaColumnProperties, String>() {{
            put( SchemaColumnProperties.DefaultValue, "US");
            put( SchemaColumnProperties.Format, "X(2)");
            put( SchemaColumnProperties.Type, "String");
        }});

    }};


    
    public TestPersonSchema() {
        
    }



    public long getBatchSize() {
        return -1;
    }

    public Collection<String> getColumnNames() {
        return repository.keySet();
    }

    public Map<SchemaColumnProperties, String> getColumnProperties(String columnName) {
        if( repository.containsKey( columnName ) ) return repository.get( columnName );
        return null;
    }

    public String getDAOId() {
        return "Test";
    }

    public int getMargin() {
        return -1;
    }

    public String getRelURL() {
        return null;
    }

    public boolean isAutosync() {
        return true;
    }

    public boolean isOpenOnInit() {
        return false;
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean isSendChangesOnly() {
        return true;
    }

    public boolean isSetFilterEveryTime() {
        return true;
    }

    public Class getIDataTypeClass() {
        return TestPerson.class;
    }

    public boolean isDynamic() {
        return false;
    }

    public IDSSchema getDataSetSchema() {
        return null;
    }
}
