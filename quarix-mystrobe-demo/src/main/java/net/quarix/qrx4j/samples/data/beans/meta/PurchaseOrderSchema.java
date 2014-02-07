package net.quarix.qrx4j.samples.data.beans.meta;

import java.util.LinkedHashMap;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.impl.DAOSchema;
import net.mystrobe.client.SchemaColumnProperties;
import java.util.Map;
import net.quarix.qrx4j.samples.data.beans.PurchaseOrder;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import java.util.HashMap;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: mystrobe
 *		 dsName: wicketds.server.dmpurchaseorder
 *		 doName: ttpurchaseorder
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmpurchaseorder", dsName="wicketds.server.dmpurchaseorder", dsId="daopurchaseorder", daoId="ttpurchaseorder", isLocked=false)
public class PurchaseOrderSchema extends DAOSchema<PurchaseOrder> {

	@Generated
	public enum Cols {
		
		SORTORDER("sortorder"),
		PONUM("ponum"),
		DATEENTERED("dateentered"),
		RECEIVEDATE("receivedate"),
		POSTATUS("postatus"),
		SUPPLIERIDNUM("supplieridnum")
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
	public PurchaseOrderSchema() {
		assignValues();
	}

	@Generated
	protected void assignValues() {
		super.assignValues();
		daoId = "ttpurchaseorder";
		iDataTypeClass = PurchaseOrder.class;
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

		Map<SchemaColumnProperties, String> ponumProp = new HashMap<SchemaColumnProperties, String>();
		ponumProp.put(SchemaColumnProperties.ReadOnly, "");
		ponumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		ponumProp.put(SchemaColumnProperties.Type, "integer");
		ponumProp.put(SchemaColumnProperties.DefaultValue, "0");
		ponumProp.put(SchemaColumnProperties.Required, "false");
		ponumProp.put(SchemaColumnProperties.Sortable, "true ");
		ponumProp.put(SchemaColumnProperties.Label, "PONum");
		ponumProp.put(SchemaColumnProperties.Tooltip, "");
		ponumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("ponum", ponumProp);

		Map<SchemaColumnProperties, String> dateenteredProp = new HashMap<SchemaColumnProperties, String>();
		dateenteredProp.put(SchemaColumnProperties.ReadOnly, "");
		dateenteredProp.put(SchemaColumnProperties.Format, "99/99/99");
		dateenteredProp.put(SchemaColumnProperties.Type, "date");
		dateenteredProp.put(SchemaColumnProperties.DefaultValue, "?");
		dateenteredProp.put(SchemaColumnProperties.Required, "false");
		dateenteredProp.put(SchemaColumnProperties.Sortable, "false");
		dateenteredProp.put(SchemaColumnProperties.Label, "DateEntered");
		dateenteredProp.put(SchemaColumnProperties.Tooltip, "");
		dateenteredProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("dateentered", dateenteredProp);

		Map<SchemaColumnProperties, String> receivedateProp = new HashMap<SchemaColumnProperties, String>();
		receivedateProp.put(SchemaColumnProperties.ReadOnly, "");
		receivedateProp.put(SchemaColumnProperties.Format, "99/99/99");
		receivedateProp.put(SchemaColumnProperties.Type, "date");
		receivedateProp.put(SchemaColumnProperties.DefaultValue, "?");
		receivedateProp.put(SchemaColumnProperties.Required, "false");
		receivedateProp.put(SchemaColumnProperties.Sortable, "false");
		receivedateProp.put(SchemaColumnProperties.Label, "ReceiveDate");
		receivedateProp.put(SchemaColumnProperties.Tooltip, "");
		receivedateProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("receivedate", receivedateProp);

		Map<SchemaColumnProperties, String> postatusProp = new HashMap<SchemaColumnProperties, String>();
		postatusProp.put(SchemaColumnProperties.ReadOnly, "");
		postatusProp.put(SchemaColumnProperties.Format, "x(8)");
		postatusProp.put(SchemaColumnProperties.Type, "character");
		postatusProp.put(SchemaColumnProperties.DefaultValue, "");
		postatusProp.put(SchemaColumnProperties.Required, "false");
		postatusProp.put(SchemaColumnProperties.Sortable, "false");
		postatusProp.put(SchemaColumnProperties.Label, "POStatus");
		postatusProp.put(SchemaColumnProperties.Tooltip, "");
		postatusProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("postatus", postatusProp);

		Map<SchemaColumnProperties, String> supplieridnumProp = new HashMap<SchemaColumnProperties, String>();
		supplieridnumProp.put(SchemaColumnProperties.ReadOnly, "");
		supplieridnumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		supplieridnumProp.put(SchemaColumnProperties.Type, "integer");
		supplieridnumProp.put(SchemaColumnProperties.DefaultValue, "0");
		supplieridnumProp.put(SchemaColumnProperties.Required, "false");
		supplieridnumProp.put(SchemaColumnProperties.Sortable, "false");
		supplieridnumProp.put(SchemaColumnProperties.Label, "SupplierIDNum");
		supplieridnumProp.put(SchemaColumnProperties.Tooltip, "");
		supplieridnumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("supplieridnum", supplieridnumProp);
	}
	
}

