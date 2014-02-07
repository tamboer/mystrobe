package net.quarix.qrx4j.samples.data.beans;

import net.mystrobe.client.IDataSource;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.impl.DAOSchema;
import net.quarix.qrx4j.samples.data.beans.meta.StateDSSchema;
import net.quarix.qrx4j.samples.data.beans.meta.StateSchema;

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
public class State  implements IDataBean {

	@Generated
	private static final long serialVersionUID = 1391676966073L;

	@Generated
	protected String rowid;
	@Generated
	protected Integer rowstate;
	@Generated
	protected Integer sortorder;
	@Generated
	protected String region;
	@Generated
	protected String statecode;
	@Generated
	protected String statename;

	@Generated
	public static Class<? extends IDSSchema> getDSSchemaClass() {
		return StateDSSchema.class;
	}

	@Generated	
	public static Class<? extends DAOSchema<State>> getDAOSchemaClass() {
		return StateSchema.class;
	}

	@Generated
	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	@Generated
	public void setRowstate(Integer rowstate) {
		this.rowstate = rowstate;
	}

	@Generated
	public Integer getRowstate() {
		return rowstate;
	}

	@Generated
	public void setSortorder(Integer sortorder) {
		this.sortorder = sortorder;
	}

	@Generated
	public Integer getSortorder() {
		return sortorder;
	}

	@Generated
	public void setRegion(String region) {
		this.region = region;
	}

	@Generated
	public String getRegion() {
		return region;
	}

	@Generated
	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

	@Generated
	public String getStatecode() {
		return statecode;
	}

	@Generated
	public void setStatename(String statename) {
		this.statename = statename;
	}

	@Generated
	public String getStatename() {
		return statename;
	}

	@Generated
	public String getRowId() {
		return rowid;
	}

	@Override
	public String toString() {
		return  " State  [  " +  " rowid =" + rowid + "\n" +  
	    ", rowstate =" + rowstate + "\n" + 
	   ", sortorder =" + sortorder + "\n" + 
	   ", region =" + region + "\n" + 
	   ", statecode =" + statecode + "\n" + 
	   ", statename =" + statename + "\n" + 
	  "] " ;
	}
	
}

