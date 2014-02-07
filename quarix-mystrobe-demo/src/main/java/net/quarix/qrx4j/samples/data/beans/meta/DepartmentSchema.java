package net.quarix.qrx4j.samples.data.beans.meta;

import java.util.LinkedHashMap;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.impl.DAOSchema;
import net.quarix.qrx4j.samples.data.beans.Department;
import net.mystrobe.client.SchemaColumnProperties;
import java.util.Map;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import java.util.HashMap;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: mystrobe
 *		 dsName: wicketds.server.dmdepartment
 *		 doName: ttdepartment
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmdepartment", dsName="wicketds.server.dmdepartment", dsId="daodepartment", daoId="ttdepartment", isLocked=false)
public class DepartmentSchema extends DAOSchema<Department> {

	@Generated
	public enum Cols {
		
		SORTORDER("sortorder"),
		DEPTCODE("deptcode"),
		DEPTNAME("deptname")
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
	public DepartmentSchema() {
		assignValues();
	}

	@Generated
	protected void assignValues() {
		super.assignValues();
		daoId = "ttdepartment";
		iDataTypeClass = Department.class;
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
		sortorderProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		sortorderProp.put(SchemaColumnProperties.Type, "integer");
		sortorderProp.put(SchemaColumnProperties.DefaultValue, "0");
		sortorderProp.put(SchemaColumnProperties.Required, "false");
		sortorderProp.put(SchemaColumnProperties.Sortable, "true ");
		sortorderProp.put(SchemaColumnProperties.Label, "SortOrder");
		sortorderProp.put(SchemaColumnProperties.Tooltip, "");
		sortorderProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("sortorder", sortorderProp);

		Map<SchemaColumnProperties, String> deptcodeProp = new HashMap<SchemaColumnProperties, String>();
		deptcodeProp.put(SchemaColumnProperties.ReadOnly, "");
		deptcodeProp.put(SchemaColumnProperties.Format, "x(8)");
		deptcodeProp.put(SchemaColumnProperties.Type, "character");
		deptcodeProp.put(SchemaColumnProperties.DefaultValue, "");
		deptcodeProp.put(SchemaColumnProperties.Required, "false");
		deptcodeProp.put(SchemaColumnProperties.Sortable, "true ");
		deptcodeProp.put(SchemaColumnProperties.Label, "DeptCode");
		deptcodeProp.put(SchemaColumnProperties.Tooltip, "");
		deptcodeProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("deptcode", deptcodeProp);

		Map<SchemaColumnProperties, String> deptnameProp = new HashMap<SchemaColumnProperties, String>();
		deptnameProp.put(SchemaColumnProperties.ReadOnly, "");
		deptnameProp.put(SchemaColumnProperties.Format, "x(8)");
		deptnameProp.put(SchemaColumnProperties.Type, "character");
		deptnameProp.put(SchemaColumnProperties.DefaultValue, "");
		deptnameProp.put(SchemaColumnProperties.Required, "false");
		deptnameProp.put(SchemaColumnProperties.Sortable, "false");
		deptnameProp.put(SchemaColumnProperties.Label, "DeptName");
		deptnameProp.put(SchemaColumnProperties.Tooltip, "");
		deptnameProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("deptname", deptnameProp);
	}
	
}

