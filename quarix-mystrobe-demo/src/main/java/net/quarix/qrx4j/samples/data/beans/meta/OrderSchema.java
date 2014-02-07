package net.quarix.qrx4j.samples.data.beans.meta;

import java.util.LinkedHashMap;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.impl.DAOSchema;
import net.mystrobe.client.SchemaColumnProperties;
import java.util.Map;
import net.quarix.qrx4j.samples.data.beans.Order;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import java.util.HashMap;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: mystrobe
 *		 dsName: wicketds.server.dmorder
 *		 doName: ttorder
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmorder", dsName="wicketds.server.dmorder", dsId="daoorder", daoId="ttorder", isLocked=false)
public class OrderSchema extends DAOSchema<Order> {

	@Generated
	public enum Cols {
		
		SORTORDER("sortorder"),
		BILLTOID("billtoid"),
		CARRIER("carrier"),
		CREDITCARD("creditcard"),
		CUSTNUM("custnum"),
		INSTRUCTIONS("instructions"),
		ORDERDATE("orderdate"),
		ORDERNUM("ordernum"),
		ORDERSTATUS("orderstatus"),
		PO("po"),
		PROMISEDATE("promisedate"),
		SALESREP("salesrep"),
		SHIPDATE("shipdate"),
		SHIPTOID("shiptoid"),
		TERMS("terms"),
		WAREHOUSENUM("warehousenum"),
		CUSTNAME("custname"),
		SALESREPNAME("salesrepname")
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
	public OrderSchema() {
		assignValues();
	}

	@Generated
	protected void assignValues() {
		super.assignValues();
		daoId = "ttorder";
		iDataTypeClass = Order.class;
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

		Map<SchemaColumnProperties, String> billtoidProp = new HashMap<SchemaColumnProperties, String>();
		billtoidProp.put(SchemaColumnProperties.ReadOnly, "");
		billtoidProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		billtoidProp.put(SchemaColumnProperties.Type, "integer");
		billtoidProp.put(SchemaColumnProperties.DefaultValue, "0");
		billtoidProp.put(SchemaColumnProperties.Required, "false");
		billtoidProp.put(SchemaColumnProperties.Sortable, "false");
		billtoidProp.put(SchemaColumnProperties.Label, "BillToID");
		billtoidProp.put(SchemaColumnProperties.Tooltip, "");
		billtoidProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("billtoid", billtoidProp);

		Map<SchemaColumnProperties, String> carrierProp = new HashMap<SchemaColumnProperties, String>();
		carrierProp.put(SchemaColumnProperties.ReadOnly, "");
		carrierProp.put(SchemaColumnProperties.Format, "x(8)");
		carrierProp.put(SchemaColumnProperties.Type, "character");
		carrierProp.put(SchemaColumnProperties.DefaultValue, "");
		carrierProp.put(SchemaColumnProperties.Required, "false");
		carrierProp.put(SchemaColumnProperties.Sortable, "false");
		carrierProp.put(SchemaColumnProperties.Label, "Carrier");
		carrierProp.put(SchemaColumnProperties.Tooltip, "");
		carrierProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("carrier", carrierProp);

		Map<SchemaColumnProperties, String> creditcardProp = new HashMap<SchemaColumnProperties, String>();
		creditcardProp.put(SchemaColumnProperties.ReadOnly, "");
		creditcardProp.put(SchemaColumnProperties.Format, "x(8)");
		creditcardProp.put(SchemaColumnProperties.Type, "character");
		creditcardProp.put(SchemaColumnProperties.DefaultValue, "");
		creditcardProp.put(SchemaColumnProperties.Required, "false");
		creditcardProp.put(SchemaColumnProperties.Sortable, "false");
		creditcardProp.put(SchemaColumnProperties.Label, "CreditCard");
		creditcardProp.put(SchemaColumnProperties.Tooltip, "");
		creditcardProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("creditcard", creditcardProp);

		Map<SchemaColumnProperties, String> custnumProp = new HashMap<SchemaColumnProperties, String>();
		custnumProp.put(SchemaColumnProperties.ReadOnly, "");
		custnumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		custnumProp.put(SchemaColumnProperties.Type, "integer");
		custnumProp.put(SchemaColumnProperties.DefaultValue, "0");
		custnumProp.put(SchemaColumnProperties.Required, "false");
		custnumProp.put(SchemaColumnProperties.Sortable, "false");
		custnumProp.put(SchemaColumnProperties.Label, "CustNum");
		custnumProp.put(SchemaColumnProperties.Tooltip, "");
		custnumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("custnum", custnumProp);

		Map<SchemaColumnProperties, String> instructionsProp = new HashMap<SchemaColumnProperties, String>();
		instructionsProp.put(SchemaColumnProperties.ReadOnly, "");
		instructionsProp.put(SchemaColumnProperties.Format, "x(8)");
		instructionsProp.put(SchemaColumnProperties.Type, "character");
		instructionsProp.put(SchemaColumnProperties.DefaultValue, "");
		instructionsProp.put(SchemaColumnProperties.Required, "false");
		instructionsProp.put(SchemaColumnProperties.Sortable, "false");
		instructionsProp.put(SchemaColumnProperties.Label, "Instructions");
		instructionsProp.put(SchemaColumnProperties.Tooltip, "");
		instructionsProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("instructions", instructionsProp);

		Map<SchemaColumnProperties, String> orderdateProp = new HashMap<SchemaColumnProperties, String>();
		orderdateProp.put(SchemaColumnProperties.ReadOnly, "");
		orderdateProp.put(SchemaColumnProperties.Format, "99/99/99");
		orderdateProp.put(SchemaColumnProperties.Type, "date");
		orderdateProp.put(SchemaColumnProperties.DefaultValue, "?");
		orderdateProp.put(SchemaColumnProperties.Required, "false");
		orderdateProp.put(SchemaColumnProperties.Sortable, "false");
		orderdateProp.put(SchemaColumnProperties.Label, "OrderDate");
		orderdateProp.put(SchemaColumnProperties.Tooltip, "");
		orderdateProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("orderdate", orderdateProp);

		Map<SchemaColumnProperties, String> ordernumProp = new HashMap<SchemaColumnProperties, String>();
		ordernumProp.put(SchemaColumnProperties.ReadOnly, "");
		ordernumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		ordernumProp.put(SchemaColumnProperties.Type, "integer");
		ordernumProp.put(SchemaColumnProperties.DefaultValue, "0");
		ordernumProp.put(SchemaColumnProperties.Required, "false");
		ordernumProp.put(SchemaColumnProperties.Sortable, "true ");
		ordernumProp.put(SchemaColumnProperties.Label, "Ordernum");
		ordernumProp.put(SchemaColumnProperties.Tooltip, "");
		ordernumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("ordernum", ordernumProp);

		Map<SchemaColumnProperties, String> orderstatusProp = new HashMap<SchemaColumnProperties, String>();
		orderstatusProp.put(SchemaColumnProperties.ReadOnly, "");
		orderstatusProp.put(SchemaColumnProperties.Format, "x(8)");
		orderstatusProp.put(SchemaColumnProperties.Type, "character");
		orderstatusProp.put(SchemaColumnProperties.DefaultValue, "");
		orderstatusProp.put(SchemaColumnProperties.Required, "false");
		orderstatusProp.put(SchemaColumnProperties.Sortable, "false");
		orderstatusProp.put(SchemaColumnProperties.Label, "OrderStatus");
		orderstatusProp.put(SchemaColumnProperties.Tooltip, "");
		orderstatusProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("orderstatus", orderstatusProp);

		Map<SchemaColumnProperties, String> poProp = new HashMap<SchemaColumnProperties, String>();
		poProp.put(SchemaColumnProperties.ReadOnly, "");
		poProp.put(SchemaColumnProperties.Format, "x(8)");
		poProp.put(SchemaColumnProperties.Type, "character");
		poProp.put(SchemaColumnProperties.DefaultValue, "");
		poProp.put(SchemaColumnProperties.Required, "false");
		poProp.put(SchemaColumnProperties.Sortable, "false");
		poProp.put(SchemaColumnProperties.Label, "PO");
		poProp.put(SchemaColumnProperties.Tooltip, "");
		poProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("po", poProp);

		Map<SchemaColumnProperties, String> promisedateProp = new HashMap<SchemaColumnProperties, String>();
		promisedateProp.put(SchemaColumnProperties.ReadOnly, "");
		promisedateProp.put(SchemaColumnProperties.Format, "99/99/99");
		promisedateProp.put(SchemaColumnProperties.Type, "date");
		promisedateProp.put(SchemaColumnProperties.DefaultValue, "?");
		promisedateProp.put(SchemaColumnProperties.Required, "false");
		promisedateProp.put(SchemaColumnProperties.Sortable, "false");
		promisedateProp.put(SchemaColumnProperties.Label, "PromiseDate");
		promisedateProp.put(SchemaColumnProperties.Tooltip, "");
		promisedateProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("promisedate", promisedateProp);

		Map<SchemaColumnProperties, String> salesrepProp = new HashMap<SchemaColumnProperties, String>();
		salesrepProp.put(SchemaColumnProperties.ReadOnly, "");
		salesrepProp.put(SchemaColumnProperties.Format, "x(8)");
		salesrepProp.put(SchemaColumnProperties.Type, "character");
		salesrepProp.put(SchemaColumnProperties.DefaultValue, "");
		salesrepProp.put(SchemaColumnProperties.Required, "false");
		salesrepProp.put(SchemaColumnProperties.Sortable, "false");
		salesrepProp.put(SchemaColumnProperties.Label, "SalesRep");
		salesrepProp.put(SchemaColumnProperties.Tooltip, "");
		salesrepProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("salesrep", salesrepProp);

		Map<SchemaColumnProperties, String> shipdateProp = new HashMap<SchemaColumnProperties, String>();
		shipdateProp.put(SchemaColumnProperties.ReadOnly, "");
		shipdateProp.put(SchemaColumnProperties.Format, "99/99/99");
		shipdateProp.put(SchemaColumnProperties.Type, "date");
		shipdateProp.put(SchemaColumnProperties.DefaultValue, "?");
		shipdateProp.put(SchemaColumnProperties.Required, "false");
		shipdateProp.put(SchemaColumnProperties.Sortable, "false");
		shipdateProp.put(SchemaColumnProperties.Label, "ShipDate");
		shipdateProp.put(SchemaColumnProperties.Tooltip, "");
		shipdateProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("shipdate", shipdateProp);

		Map<SchemaColumnProperties, String> shiptoidProp = new HashMap<SchemaColumnProperties, String>();
		shiptoidProp.put(SchemaColumnProperties.ReadOnly, "");
		shiptoidProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		shiptoidProp.put(SchemaColumnProperties.Type, "integer");
		shiptoidProp.put(SchemaColumnProperties.DefaultValue, "0");
		shiptoidProp.put(SchemaColumnProperties.Required, "false");
		shiptoidProp.put(SchemaColumnProperties.Sortable, "false");
		shiptoidProp.put(SchemaColumnProperties.Label, "ShipToID");
		shiptoidProp.put(SchemaColumnProperties.Tooltip, "");
		shiptoidProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("shiptoid", shiptoidProp);

		Map<SchemaColumnProperties, String> termsProp = new HashMap<SchemaColumnProperties, String>();
		termsProp.put(SchemaColumnProperties.ReadOnly, "");
		termsProp.put(SchemaColumnProperties.Format, "x(8)");
		termsProp.put(SchemaColumnProperties.Type, "character");
		termsProp.put(SchemaColumnProperties.DefaultValue, "");
		termsProp.put(SchemaColumnProperties.Required, "false");
		termsProp.put(SchemaColumnProperties.Sortable, "false");
		termsProp.put(SchemaColumnProperties.Label, "Terms");
		termsProp.put(SchemaColumnProperties.Tooltip, "");
		termsProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("terms", termsProp);

		Map<SchemaColumnProperties, String> warehousenumProp = new HashMap<SchemaColumnProperties, String>();
		warehousenumProp.put(SchemaColumnProperties.ReadOnly, "");
		warehousenumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		warehousenumProp.put(SchemaColumnProperties.Type, "integer");
		warehousenumProp.put(SchemaColumnProperties.DefaultValue, "0");
		warehousenumProp.put(SchemaColumnProperties.Required, "false");
		warehousenumProp.put(SchemaColumnProperties.Sortable, "false");
		warehousenumProp.put(SchemaColumnProperties.Label, "WarehouseNum");
		warehousenumProp.put(SchemaColumnProperties.Tooltip, "");
		warehousenumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("warehousenum", warehousenumProp);

		Map<SchemaColumnProperties, String> custnameProp = new HashMap<SchemaColumnProperties, String>();
		custnameProp.put(SchemaColumnProperties.ReadOnly, "");
		custnameProp.put(SchemaColumnProperties.Format, "x(8)");
		custnameProp.put(SchemaColumnProperties.Type, "character");
		custnameProp.put(SchemaColumnProperties.DefaultValue, "");
		custnameProp.put(SchemaColumnProperties.Required, "false");
		custnameProp.put(SchemaColumnProperties.Sortable, "false");
		custnameProp.put(SchemaColumnProperties.Label, "CustName");
		custnameProp.put(SchemaColumnProperties.Tooltip, "");
		custnameProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("custname", custnameProp);

		Map<SchemaColumnProperties, String> salesrepnameProp = new HashMap<SchemaColumnProperties, String>();
		salesrepnameProp.put(SchemaColumnProperties.ReadOnly, "");
		salesrepnameProp.put(SchemaColumnProperties.Format, "x(8)");
		salesrepnameProp.put(SchemaColumnProperties.Type, "character");
		salesrepnameProp.put(SchemaColumnProperties.DefaultValue, "");
		salesrepnameProp.put(SchemaColumnProperties.Required, "false");
		salesrepnameProp.put(SchemaColumnProperties.Sortable, "false");
		salesrepnameProp.put(SchemaColumnProperties.Label, "SalesRepName");
		salesrepnameProp.put(SchemaColumnProperties.Tooltip, "");
		salesrepnameProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("salesrepname", salesrepnameProp);
	}
	
}

