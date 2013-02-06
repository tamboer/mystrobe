package net.quarix.qrx4j.samples.data.dao;

import net.quarix.qrx4j.samples.data.beans.meta.PurchaseOrderSchema;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.quarix.qrx4j.samples.data.beans.PurchaseOrder;
import net.quarix.qrx4j.samples.data.beans.meta.PurchaseOrderDSSchema;
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
 *		 dsName: wicketds.server.dmpurchaseorder
 *		 doName: ttpurchaseorder
 *
 */

@GeneratorMeta(appName="qrxexmpl", urn="wicketds.server.dmpurchaseorder", dsName="wicketds.server.dmpurchaseorder", dsId="daopurchaseorder", daoId="ttpurchaseorder", isLocked=false)
public class PurchaseOrderDataObject extends DataObjectAdaptor<PurchaseOrder> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1357749071366L;

	@Generated
	@Override
	protected void assignValues () {
		this.schema = new PurchaseOrderSchema();
		this.defaultDSSchema= new PurchaseOrderDSSchema();  
	}
	
	
}
