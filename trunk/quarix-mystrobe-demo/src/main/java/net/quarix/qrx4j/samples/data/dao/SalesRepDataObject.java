package net.quarix.qrx4j.samples.data.dao;

import net.mystrobe.client.connector.quarixbackend.Generated;
import net.quarix.qrx4j.samples.data.beans.meta.SalesRepSchema;
import net.quarix.qrx4j.samples.data.beans.meta.SalesRepDSSchema;
import net.quarix.qrx4j.samples.data.beans.SalesRep;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.IDAOSchema;
import java.io.Serializable;
import net.mystrobe.client.DataObjectAdaptor;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: qrxexmpl
 *		 dsName: wicketds.server.dmsalesrep
 *		 doName: ttsalesrep
 *
 */

@GeneratorMeta(appName="qrxexmpl", urn="wicketds.server.dmsalesrep", dsName="wicketds.server.dmsalesrep", dsId="daosalesrep", daoId="ttsalesrep", isLocked=false)
public class SalesRepDataObject extends DataObjectAdaptor<SalesRep> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1358941709673L;

	@Generated
	@Override
	protected void assignValues () {
		this.schema = new SalesRepSchema();
		this.defaultDSSchema= new SalesRepDSSchema();  
	}
	
	
}
