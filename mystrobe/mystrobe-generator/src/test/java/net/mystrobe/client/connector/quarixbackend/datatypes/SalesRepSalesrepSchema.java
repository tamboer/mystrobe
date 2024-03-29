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
 package net.mystrobe.client.connector.quarixbackend.datatypes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.mystrobe.client.SchemaColumnProperties;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.impl.DAOSchema;

/**
 * 
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 * 
 * Used parameters: appName: wicketds dsName: server.salesrep doName: tt-salesrep
 * 
 */

@GeneratorMeta(appName="wicketds", urn="server.salesrep", dsName="server.salesrep", dsId="dsSalesRep", daoId="tt-salesrep", isLocked=false)
public class SalesRepSalesrepSchema extends DAOSchema<SalesRepSalesrep> {

	@Generated
	public enum Cols {
		
		SALESREP("sales-rep"),
		REPNAME("rep-name"),
		REGION("region"),
		MONTHQUOTA_1("month-quota_1"),
		MONTHQUOTA_2("month-quota_2"),
		MONTHQUOTA_3("month-quota_3"),
		MONTHQUOTA_4("month-quota_4"),
		MONTHQUOTA_5("month-quota_5"),
		MONTHQUOTA_6("month-quota_6"),
		MONTHQUOTA_7("month-quota_7"),
		MONTHQUOTA_8("month-quota_8"),
		MONTHQUOTA_9("month-quota_9"),
		MONTHQUOTA_10("month-quota_10"),
		MONTHQUOTA_11("month-quota_11"),
		MONTHQUOTA_12("month-quota_12")
		;

		private String columnName;
			
		private Cols(String columnName) {
			this.columnName = columnName;
		}

		public String id() {
			return columnName;
		}	

		public String toString() {
			return columnName;
		}
	}

	@Generated
	public SalesRepSalesrepSchema() {
		this(new SalesRepDSSchema());
	}

	@Generated
	public SalesRepSalesrepSchema(SalesRepDSSchema dsSchema) {
		super();
		assignValues();	
	}

	@Generated
	protected void assignValues() {
		super.assignValues();
		daoId = "tt-salesrep";
		iDataTypeClass = SalesRepSalesrep.class;
		batchSize = 50;
		margin = 5;
		isAutosync = true;
		isOpenOnInit = true;
		isSendChangesOnly = true;
		isSetFilterEveryTime = true;

		properties = new LinkedHashMap<String, Map<SchemaColumnProperties, String>>();

		Map<SchemaColumnProperties, String> rowidProp = new HashMap<SchemaColumnProperties, String>();
		rowidProp.put(SchemaColumnProperties.ReadOnly, "");
		rowidProp.put(SchemaColumnProperties.Format, "");
		rowidProp.put(SchemaColumnProperties.Type, "character");
		rowidProp.put(SchemaColumnProperties.DefaultValue, "");
		rowidProp.put(SchemaColumnProperties.Required, "");
		rowidProp.put(SchemaColumnProperties.Sortable, "");
		rowidProp.put(SchemaColumnProperties.Label, "");
		rowidProp.put(SchemaColumnProperties.Tooltip, "");
		rowidProp.put(SchemaColumnProperties.ViewAs, "");
		properties.put("rowid", rowidProp);

		Map<SchemaColumnProperties, String> rowstateProp = new HashMap<SchemaColumnProperties, String>();
		rowstateProp.put(SchemaColumnProperties.ReadOnly, "");
		rowstateProp.put(SchemaColumnProperties.Format, "");
		rowstateProp.put(SchemaColumnProperties.Type, "integer");
		rowstateProp.put(SchemaColumnProperties.DefaultValue, "0");
		rowstateProp.put(SchemaColumnProperties.Required, "");
		rowstateProp.put(SchemaColumnProperties.Sortable, "");
		rowstateProp.put(SchemaColumnProperties.Label, "");
		rowstateProp.put(SchemaColumnProperties.Tooltip, "");
		rowstateProp.put(SchemaColumnProperties.ViewAs, "");
		properties.put("rowstate", rowstateProp);

		Map<SchemaColumnProperties, String> salesrepProp = new HashMap<SchemaColumnProperties, String>();
		salesrepProp.put(SchemaColumnProperties.ReadOnly, "");
		salesrepProp.put(SchemaColumnProperties.Format, "x(4)");
		salesrepProp.put(SchemaColumnProperties.Type, "character");
		salesrepProp.put(SchemaColumnProperties.DefaultValue, "");
		salesrepProp.put(SchemaColumnProperties.Required, "false");
		salesrepProp.put(SchemaColumnProperties.Sortable, "true ");
		salesrepProp.put(SchemaColumnProperties.Label, "Sales-Rep");
		salesrepProp.put(SchemaColumnProperties.Tooltip, "");
		salesrepProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("sales-rep", salesrepProp);

		Map<SchemaColumnProperties, String> repnameProp = new HashMap<SchemaColumnProperties, String>();
		repnameProp.put(SchemaColumnProperties.ReadOnly, "");
		repnameProp.put(SchemaColumnProperties.Format, "x(30)");
		repnameProp.put(SchemaColumnProperties.Type, "character");
		repnameProp.put(SchemaColumnProperties.DefaultValue, "");
		repnameProp.put(SchemaColumnProperties.Required, "false");
		repnameProp.put(SchemaColumnProperties.Sortable, "false");
		repnameProp.put(SchemaColumnProperties.Label, "Rep-Name");
		repnameProp.put(SchemaColumnProperties.Tooltip, "Name of Salesman");
		repnameProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("rep-name", repnameProp);

		Map<SchemaColumnProperties, String> regionProp = new HashMap<SchemaColumnProperties, String>();
		regionProp.put(SchemaColumnProperties.ReadOnly, "");
		regionProp.put(SchemaColumnProperties.Format, "x(8)");
		regionProp.put(SchemaColumnProperties.Type, "character");
		regionProp.put(SchemaColumnProperties.DefaultValue, "");
		regionProp.put(SchemaColumnProperties.Required, "false");
		regionProp.put(SchemaColumnProperties.Sortable, "false");
		regionProp.put(SchemaColumnProperties.Label, "Region");
		regionProp.put(SchemaColumnProperties.Tooltip, "Sales Region covered by this salesman");
		regionProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("region", regionProp);

		Map<SchemaColumnProperties, String> monthquota_1Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_1Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_1Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_1Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_1Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_1Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_1Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_1Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_1Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_1Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_1", monthquota_1Prop);

		Map<SchemaColumnProperties, String> monthquota_2Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_2Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_2Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_2Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_2Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_2Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_2Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_2Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_2Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_2Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_2", monthquota_2Prop);

		Map<SchemaColumnProperties, String> monthquota_3Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_3Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_3Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_3Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_3Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_3Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_3Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_3Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_3Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_3Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_3", monthquota_3Prop);

		Map<SchemaColumnProperties, String> monthquota_4Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_4Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_4Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_4Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_4Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_4Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_4Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_4Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_4Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_4Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_4", monthquota_4Prop);

		Map<SchemaColumnProperties, String> monthquota_5Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_5Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_5Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_5Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_5Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_5Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_5Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_5Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_5Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_5Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_5", monthquota_5Prop);

		Map<SchemaColumnProperties, String> monthquota_6Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_6Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_6Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_6Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_6Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_6Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_6Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_6Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_6Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_6Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_6", monthquota_6Prop);

		Map<SchemaColumnProperties, String> monthquota_7Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_7Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_7Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_7Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_7Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_7Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_7Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_7Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_7Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_7Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_7", monthquota_7Prop);

		Map<SchemaColumnProperties, String> monthquota_8Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_8Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_8Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_8Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_8Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_8Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_8Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_8Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_8Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_8Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_8", monthquota_8Prop);

		Map<SchemaColumnProperties, String> monthquota_9Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_9Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_9Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_9Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_9Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_9Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_9Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_9Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_9Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_9Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_9", monthquota_9Prop);

		Map<SchemaColumnProperties, String> monthquota_10Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_10Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_10Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_10Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_10Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_10Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_10Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_10Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_10Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_10Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_10", monthquota_10Prop);

		Map<SchemaColumnProperties, String> monthquota_11Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_11Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_11Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_11Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_11Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_11Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_11Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_11Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_11Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_11Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_11", monthquota_11Prop);

		Map<SchemaColumnProperties, String> monthquota_12Prop = new HashMap<SchemaColumnProperties, String>();
		monthquota_12Prop.put(SchemaColumnProperties.ReadOnly, "");
		monthquota_12Prop.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		monthquota_12Prop.put(SchemaColumnProperties.Type, "integer");
		monthquota_12Prop.put(SchemaColumnProperties.DefaultValue, "0");
		monthquota_12Prop.put(SchemaColumnProperties.Required, "false");
		monthquota_12Prop.put(SchemaColumnProperties.Sortable, "false");
		monthquota_12Prop.put(SchemaColumnProperties.Label, "Month-Quota");
		monthquota_12Prop.put(SchemaColumnProperties.Tooltip, "");
		monthquota_12Prop.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("month-quota_12", monthquota_12Prop);
	}
	
	
}

