package net.quarix.qrx4j.samples.data.beans.meta;

import net.mystrobe.client.impl.DSSchema;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.Generated;

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

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmstate", dsName="wicketds.server.dmstate", dsId="daostate", isLocked=false)
public class StateDSSchema extends DSSchema {

	@Generated
	public StateDSSchema() {
		id = "daostate";
		batchSize = 0;
		margin = 5;
		urn = "wicketds.server.dmstate";
		hasFilteredChildren = true;

		loadRelations(new String[] {});

		daoMap.put("ttstate", new StateSchema());
	}
	
}
