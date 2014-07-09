package net.quarix.qrx4j.samples.data.dao;

import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.IConfig;
import net.quarix.qrx4j.samples.data.beans.Customer;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.IDAOSchema;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerDSSchema;
import net.quarix.qrx4j.samples.data.beans.meta.CustomerSchema;
import java.io.Serializable;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.DataObjectAdaptor;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: mystrobe
 *		 dsName: wicketds.server.dmcustomer
 *		 doName: ttcustomer
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmcustomer", dsName="wicketds.server.dmcustomer", dsId="daocustomer", daoId="ttcustomer", isLocked=false)
public class CustomerDataObject extends DataObjectAdaptor<Customer> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1391676527697L;
		
	@Generated
	@Deprecated
	public CustomerDataObject (IAppConnector connector) {
		super(connector, new CustomerDSSchema(), new CustomerSchema());
	}

	@Generated
	public CustomerDataObject (IConfig config, String appName) {
		super(config, appName, new CustomerDSSchema(), new CustomerSchema());
	}

	@Generated
	@Deprecated
	public CustomerDataObject (IAppConnector connector, IDSSchema dsSchema, IDAOSchema<Customer> daoSchema ) {
		super(connector, dsSchema, daoSchema);
	}

	@Generated
	public CustomerDataObject (IConfig config, String appName, IDSSchema dsSchema, IDAOSchema<Customer> daoSchema ) {
		super(config, appName, dsSchema, daoSchema);
	}
	
}
