package net.quarix.qrx4j.samples.data.dao;

import net.quarix.qrx4j.samples.data.beans.meta.StateSchema;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.IConfig;
import net.quarix.qrx4j.samples.data.beans.State;
import net.mystrobe.client.connector.IAppConnector;
import net.quarix.qrx4j.samples.data.beans.meta.StateDSSchema;
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
 *		 dsName: wicketds.server.dmstate
 *		 doName: ttstate
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmstate", dsName="wicketds.server.dmstate", dsId="daostate", daoId="ttstate", isLocked=false)
public class StateDataObject extends DataObjectAdaptor<State> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1391676966048L;
		
	@Generated
	@Deprecated
	public StateDataObject (IAppConnector connector) {
		super(connector, new StateDSSchema(), new StateSchema());
	}

	@Generated
	public StateDataObject (IConfig config, String appName) {
		super(config, appName, new StateDSSchema(), new StateSchema());
	}

	@Generated
	@Deprecated
	public StateDataObject (IAppConnector connector, IDSSchema dsSchema, IDAOSchema<State> daoSchema ) {
		super(connector, dsSchema, daoSchema);
	}

	@Generated
	public StateDataObject (IConfig config, String appName, IDSSchema dsSchema, IDAOSchema<State> daoSchema ) {
		super(config, appName, dsSchema, daoSchema);
	}
	
}

