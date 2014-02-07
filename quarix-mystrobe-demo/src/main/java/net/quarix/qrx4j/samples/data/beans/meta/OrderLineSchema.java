package net.quarix.qrx4j.samples.data.beans.meta;

import net.quarix.qrx4j.samples.data.beans.OrderLine;
import java.util.LinkedHashMap;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.impl.DAOSchema;
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
 *		 dsName: wicketds.server.dmorderline
 *		 doName: ttorderline
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmorderline", dsName="wicketds.server.dmorderline", dsId="daoorderline", daoId="ttorderline", isLocked=false)
public class OrderLineSchema extends DAOSchema<OrderLine> {

	@Generated
	public enum Cols {
		
		SORTORDER("sortorder"),
		DISCOUNT("discount"),
		EXTENDEDPRICE("extendedprice"),
		ITEMNUM("itemnum"),
		LINENUM("linenum"),
		ORDERLINESTATUS("orderlinestatus"),
		ORDERNUM("ordernum"),
		PRICE("price"),
		QTY("qty"),
		ITEMNAME("itemname")
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
	public OrderLineSchema() {
		assignValues();
	}

	@Generated
	protected void assignValues() {
		super.assignValues();
		daoId = "ttorderline";
		iDataTypeClass = OrderLine.class;
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

		Map<SchemaColumnProperties, String> discountProp = new HashMap<SchemaColumnProperties, String>();
		discountProp.put(SchemaColumnProperties.ReadOnly, "");
		discountProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		discountProp.put(SchemaColumnProperties.Type, "integer");
		discountProp.put(SchemaColumnProperties.DefaultValue, "0");
		discountProp.put(SchemaColumnProperties.Required, "false");
		discountProp.put(SchemaColumnProperties.Sortable, "false");
		discountProp.put(SchemaColumnProperties.Label, "Discount");
		discountProp.put(SchemaColumnProperties.Tooltip, "");
		discountProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("discount", discountProp);

		Map<SchemaColumnProperties, String> extendedpriceProp = new HashMap<SchemaColumnProperties, String>();
		extendedpriceProp.put(SchemaColumnProperties.ReadOnly, "");
		extendedpriceProp.put(SchemaColumnProperties.Format, "->>,>>9.99");
		extendedpriceProp.put(SchemaColumnProperties.Type, "decimal");
		extendedpriceProp.put(SchemaColumnProperties.DefaultValue, "0,00");
		extendedpriceProp.put(SchemaColumnProperties.Required, "false");
		extendedpriceProp.put(SchemaColumnProperties.Sortable, "false");
		extendedpriceProp.put(SchemaColumnProperties.Label, "ExtendedPrice");
		extendedpriceProp.put(SchemaColumnProperties.Tooltip, "");
		extendedpriceProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("extendedprice", extendedpriceProp);

		Map<SchemaColumnProperties, String> itemnumProp = new HashMap<SchemaColumnProperties, String>();
		itemnumProp.put(SchemaColumnProperties.ReadOnly, "");
		itemnumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		itemnumProp.put(SchemaColumnProperties.Type, "integer");
		itemnumProp.put(SchemaColumnProperties.DefaultValue, "0");
		itemnumProp.put(SchemaColumnProperties.Required, "false");
		itemnumProp.put(SchemaColumnProperties.Sortable, "false");
		itemnumProp.put(SchemaColumnProperties.Label, "Itemnum");
		itemnumProp.put(SchemaColumnProperties.Tooltip, "");
		itemnumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("itemnum", itemnumProp);

		Map<SchemaColumnProperties, String> linenumProp = new HashMap<SchemaColumnProperties, String>();
		linenumProp.put(SchemaColumnProperties.ReadOnly, "");
		linenumProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		linenumProp.put(SchemaColumnProperties.Type, "integer");
		linenumProp.put(SchemaColumnProperties.DefaultValue, "0");
		linenumProp.put(SchemaColumnProperties.Required, "false");
		linenumProp.put(SchemaColumnProperties.Sortable, "true ");
		linenumProp.put(SchemaColumnProperties.Label, "Linenum");
		linenumProp.put(SchemaColumnProperties.Tooltip, "");
		linenumProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("linenum", linenumProp);

		Map<SchemaColumnProperties, String> orderlinestatusProp = new HashMap<SchemaColumnProperties, String>();
		orderlinestatusProp.put(SchemaColumnProperties.ReadOnly, "");
		orderlinestatusProp.put(SchemaColumnProperties.Format, "x(8)");
		orderlinestatusProp.put(SchemaColumnProperties.Type, "character");
		orderlinestatusProp.put(SchemaColumnProperties.DefaultValue, "");
		orderlinestatusProp.put(SchemaColumnProperties.Required, "false");
		orderlinestatusProp.put(SchemaColumnProperties.Sortable, "false");
		orderlinestatusProp.put(SchemaColumnProperties.Label, "OrderLineStatus");
		orderlinestatusProp.put(SchemaColumnProperties.Tooltip, "");
		orderlinestatusProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("orderlinestatus", orderlinestatusProp);

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

		Map<SchemaColumnProperties, String> priceProp = new HashMap<SchemaColumnProperties, String>();
		priceProp.put(SchemaColumnProperties.ReadOnly, "");
		priceProp.put(SchemaColumnProperties.Format, "->>,>>9.99");
		priceProp.put(SchemaColumnProperties.Type, "decimal");
		priceProp.put(SchemaColumnProperties.DefaultValue, "0,00");
		priceProp.put(SchemaColumnProperties.Required, "false");
		priceProp.put(SchemaColumnProperties.Sortable, "false");
		priceProp.put(SchemaColumnProperties.Label, "Price");
		priceProp.put(SchemaColumnProperties.Tooltip, "");
		priceProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("price", priceProp);

		Map<SchemaColumnProperties, String> qtyProp = new HashMap<SchemaColumnProperties, String>();
		qtyProp.put(SchemaColumnProperties.ReadOnly, "");
		qtyProp.put(SchemaColumnProperties.Format, "->,>>>,>>9");
		qtyProp.put(SchemaColumnProperties.Type, "integer");
		qtyProp.put(SchemaColumnProperties.DefaultValue, "0");
		qtyProp.put(SchemaColumnProperties.Required, "false");
		qtyProp.put(SchemaColumnProperties.Sortable, "false");
		qtyProp.put(SchemaColumnProperties.Label, "Qty");
		qtyProp.put(SchemaColumnProperties.Tooltip, "");
		qtyProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("qty", qtyProp);

		Map<SchemaColumnProperties, String> itemnameProp = new HashMap<SchemaColumnProperties, String>();
		itemnameProp.put(SchemaColumnProperties.ReadOnly, "");
		itemnameProp.put(SchemaColumnProperties.Format, "x(8)");
		itemnameProp.put(SchemaColumnProperties.Type, "character");
		itemnameProp.put(SchemaColumnProperties.DefaultValue, "");
		itemnameProp.put(SchemaColumnProperties.Required, "false");
		itemnameProp.put(SchemaColumnProperties.Sortable, "false");
		itemnameProp.put(SchemaColumnProperties.Label, "ItemName");
		itemnameProp.put(SchemaColumnProperties.Tooltip, "");
		itemnameProp.put(SchemaColumnProperties.ViewAs, "fill-in");
		properties.put("itemname", itemnameProp);
	}
	
}

