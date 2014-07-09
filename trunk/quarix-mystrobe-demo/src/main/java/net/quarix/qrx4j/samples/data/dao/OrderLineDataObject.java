package net.quarix.qrx4j.samples.data.dao;

import net.quarix.qrx4j.samples.data.beans.meta.OrderLineSchema;
import net.quarix.qrx4j.samples.data.beans.OrderLine;
import net.quarix.qrx4j.samples.data.beans.meta.OrderLineDSSchema;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.IDAOSchema;
import java.io.Serializable;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.DataObjectAdaptor;

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
public class OrderLineDataObject extends DataObjectAdaptor<OrderLine> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1391676437262L;
		
	@Generated
	@Deprecated
	public OrderLineDataObject (IAppConnector connector) {
		super(connector, new OrderLineDSSchema(), new OrderLineSchema());
	}

	@Generated
	public OrderLineDataObject (IConfig config, String appName) {
		super(config, appName, new OrderLineDSSchema(), new OrderLineSchema());
	}

	@Generated
	@Deprecated
	public OrderLineDataObject (IAppConnector connector, IDSSchema dsSchema, IDAOSchema<OrderLine> daoSchema ) {
		super(connector, dsSchema, daoSchema);
	}

	@Generated
	public OrderLineDataObject (IConfig config, String appName, IDSSchema dsSchema, IDAOSchema<OrderLine> daoSchema ) {
		super(config, appName, dsSchema, daoSchema);
	}
	
}
