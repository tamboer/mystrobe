package net.quarix.qrx4j.samples.data.dao;

import net.mystrobe.client.connector.quarixbackend.Generated;
import net.quarix.qrx4j.samples.data.beans.Department;
import net.quarix.qrx4j.samples.data.beans.meta.DepartmentDSSchema;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.IDAOSchema;
import java.io.Serializable;
import net.quarix.qrx4j.samples.data.beans.meta.DepartmentSchema;
import net.mystrobe.client.DataObjectAdaptor;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: qrxexmpl
 *		 dsName: wicketds.server.dmdepartment
 *		 doName: ttdepartment
 *
 */

@GeneratorMeta(appName="qrxexmpl", urn="wicketds.server.dmdepartment", dsName="wicketds.server.dmdepartment", dsId="daodepartment", daoId="ttdepartment", isLocked=false)
public class DepartmentDataObject extends DataObjectAdaptor<Department> implements Serializable {

	@Generated
	private static final long serialVersionUID = 1358516274134L;

	@Generated
	@Override
	protected void assignValues () {
		this.schema = new DepartmentSchema();
		this.defaultDSSchema= new DepartmentDSSchema();  
	}
	
	
}
