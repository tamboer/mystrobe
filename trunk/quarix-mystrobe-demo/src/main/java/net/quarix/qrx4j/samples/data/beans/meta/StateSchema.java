package net.quarix.qrx4j.samples.data.beans.meta;

import java.util.LinkedHashMap;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.impl.DAOSchema;
import net.mystrobe.client.SchemaColumnProperties;
import java.util.Map;
import net.quarix.qrx4j.samples.data.beans.State;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import java.util.HashMap;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: qrxexmpl
 *		 dsName: wicketds.server.dmstate
 *		 doName: ttstate
 *
 */

@GeneratorMeta(appName="qrxexmpl", urn="wicketds.server.dmstate", dsName="wicketds.server.dmstate", dsId="daostate", daoId="ttstate", isLocked=false)
public class StateSchema extends DAOSchema<State> {

	@Generated
	public enum Cols {
		
		SORTORDER("sortorder"),
		REGION("region"),
		STATECODE("statecode"),
		STATENAME("statename")
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
	public StateSchema() {
		asignValues();
	}

	@Generated
	private void asignValues() {
		daoId = "ttstate";
		iDataTypeClass = State.class;
		batchSize = 50;
		margin = 5;
		isAutosync = true;
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

		Map<SchemaColumnProperties, String> sortorderProp = new HashMap<SchemaColumnProperties, String>();
		sortorderProp.put(SchemaColumnProperties.ReadOnly, "");
		sortorderProp.put(SchemaColumnProperties.Format, "->.>>>.>>9");
		sortorderProp.put(SchemaColumnProperties.Type, "integer");
		sortorderProp.put(SchemaColumnProperties.DefaultValue, "0");
		sortorderProp.put(SchemaColumnProperties.Required, "false");
		sortorderProp.put(SchemaColumnProperties.Sortable, "true ");
		sortorderProp.put(SchemaColumnProperties.Label, "SortOrder");
		sortorderProp.put(SchemaColumnProperties.Tooltip, "");
		sortorderProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("sortorder", sortorderProp);

		Map<SchemaColumnProperties, String> regionProp = new HashMap<SchemaColumnProperties, String>();
		regionProp.put(SchemaColumnProperties.ReadOnly, "");
		regionProp.put(SchemaColumnProperties.Format, "x(8)");
		regionProp.put(SchemaColumnProperties.Type, "character");
		regionProp.put(SchemaColumnProperties.DefaultValue, "");
		regionProp.put(SchemaColumnProperties.Required, "false");
		regionProp.put(SchemaColumnProperties.Sortable, "false");
		regionProp.put(SchemaColumnProperties.Label, "Region");
		regionProp.put(SchemaColumnProperties.Tooltip, "");
		regionProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("region", regionProp);

		Map<SchemaColumnProperties, String> statecodeProp = new HashMap<SchemaColumnProperties, String>();
		statecodeProp.put(SchemaColumnProperties.ReadOnly, "");
		statecodeProp.put(SchemaColumnProperties.Format, "x(8)");
		statecodeProp.put(SchemaColumnProperties.Type, "character");
		statecodeProp.put(SchemaColumnProperties.DefaultValue, "");
		statecodeProp.put(SchemaColumnProperties.Required, "false");
		statecodeProp.put(SchemaColumnProperties.Sortable, "true ");
		statecodeProp.put(SchemaColumnProperties.Label, "StateCode");
		statecodeProp.put(SchemaColumnProperties.Tooltip, "");
		statecodeProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("statecode", statecodeProp);

		Map<SchemaColumnProperties, String> statenameProp = new HashMap<SchemaColumnProperties, String>();
		statenameProp.put(SchemaColumnProperties.ReadOnly, "");
		statenameProp.put(SchemaColumnProperties.Format, "x(8)");
		statenameProp.put(SchemaColumnProperties.Type, "character");
		statenameProp.put(SchemaColumnProperties.DefaultValue, "");
		statenameProp.put(SchemaColumnProperties.Required, "false");
		statenameProp.put(SchemaColumnProperties.Sortable, "false");
		statenameProp.put(SchemaColumnProperties.Label, "StateName");
		statenameProp.put(SchemaColumnProperties.Tooltip, "");
		statenameProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("statename", statenameProp);
	}
	
	
}

