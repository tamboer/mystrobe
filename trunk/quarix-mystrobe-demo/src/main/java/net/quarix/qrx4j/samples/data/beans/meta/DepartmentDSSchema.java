package net.quarix.qrx4j.samples.data.beans.meta;

import java.util.Collections;
import net.mystrobe.client.impl.DSSchema;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.Generated;

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

@GeneratorMeta(appName="qrxexmpl", urn="wicketds.server.dmdepartment", dsName="wicketds.server.dmdepartment", dsId="daodepartment", isLocked=false)
public class DepartmentDSSchema extends DSSchema {

	@Generated
	public DepartmentDSSchema() {
		id = "daodepartment";
		bathchSize = 0;
		margin = 5;
		urn = "wicketds.server.dmdepartment";
		hasFilteredChildren = true;

		loadRelations(new String[] {});

		daoMap.put("ttdepartment", new DepartmentSchema());
	}
	
	
}

