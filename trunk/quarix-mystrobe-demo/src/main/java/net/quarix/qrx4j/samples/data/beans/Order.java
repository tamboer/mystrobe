package net.quarix.qrx4j.samples.data.beans;

import java.util.Date;
import net.mystrobe.client.IDataSource;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.impl.DAOSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderDSSchema;
import net.quarix.qrx4j.samples.data.beans.meta.OrderSchema;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: mystrobe
 *		 dsName: wicketds.server.dmorder
 *		 doName: ttorder
 *
 */

@GeneratorMeta(appName="mystrobe", urn="wicketds.server.dmorder", dsName="wicketds.server.dmorder", dsId="daoorder", daoId="ttorder", isLocked=false)
public class Order  implements IDataBean {

	@Generated
	private static final long serialVersionUID = 1391676325059L;

	@Generated
	protected String rowid;
	@Generated
	protected Integer rowstate;
	@Generated
	protected Integer sortorder;
	@Generated
	protected Integer billtoid;
	@Generated
	protected String carrier;
	@Generated
	protected String creditcard;
	@Generated
	protected Integer custnum;
	@Generated
	protected String instructions;
	@Generated
	protected Date orderdate;
	@Generated
	protected Integer ordernum;
	@Generated
	protected String orderstatus;
	@Generated
	protected String po;
	@Generated
	protected Date promisedate;
	@Generated
	protected String salesrep;
	@Generated
	protected Date shipdate;
	@Generated
	protected Integer shiptoid;
	@Generated
	protected String terms;
	@Generated
	protected Integer warehousenum;
	@Generated
	protected String custname;
	@Generated
	protected String salesrepname;

	@Generated
	public static Class<? extends IDSSchema> getDSSchemaClass() {
		return OrderDSSchema.class;
	}

	@Generated	
	public static Class<? extends DAOSchema<Order>> getDAOSchemaClass() {
		return OrderSchema.class;
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
	public void setBilltoid(Integer billtoid) {
		this.billtoid = billtoid;
	}

	@Generated
	public Integer getBilltoid() {
		return billtoid;
	}

	@Generated
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	@Generated
	public String getCarrier() {
		return carrier;
	}

	@Generated
	public void setCreditcard(String creditcard) {
		this.creditcard = creditcard;
	}

	@Generated
	public String getCreditcard() {
		return creditcard;
	}

	@Generated
	public void setCustnum(Integer custnum) {
		this.custnum = custnum;
	}

	@Generated
	public Integer getCustnum() {
		return custnum;
	}

	@Generated
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@Generated
	public String getInstructions() {
		return instructions;
	}

	@Generated
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	@Generated
	public Date getOrderdate() {
		return orderdate;
	}

	@Generated
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	@Generated
	public Integer getOrdernum() {
		return ordernum;
	}

	@Generated
	public void setOrderstatus(String orderstatus) {
		this.orderstatus = orderstatus;
	}

	@Generated
	public String getOrderstatus() {
		return orderstatus;
	}

	@Generated
	public void setPo(String po) {
		this.po = po;
	}

	@Generated
	public String getPo() {
		return po;
	}

	@Generated
	public void setPromisedate(Date promisedate) {
		this.promisedate = promisedate;
	}

	@Generated
	public Date getPromisedate() {
		return promisedate;
	}

	@Generated
	public void setSalesrep(String salesrep) {
		this.salesrep = salesrep;
	}

	@Generated
	public String getSalesrep() {
		return salesrep;
	}

	@Generated
	public void setShipdate(Date shipdate) {
		this.shipdate = shipdate;
	}

	@Generated
	public Date getShipdate() {
		return shipdate;
	}

	@Generated
	public void setShiptoid(Integer shiptoid) {
		this.shiptoid = shiptoid;
	}

	@Generated
	public Integer getShiptoid() {
		return shiptoid;
	}

	@Generated
	public void setTerms(String terms) {
		this.terms = terms;
	}

	@Generated
	public String getTerms() {
		return terms;
	}

	@Generated
	public void setWarehousenum(Integer warehousenum) {
		this.warehousenum = warehousenum;
	}

	@Generated
	public Integer getWarehousenum() {
		return warehousenum;
	}

	@Generated
	public void setCustname(String custname) {
		this.custname = custname;
	}

	@Generated
	public String getCustname() {
		return custname;
	}

	@Generated
	public void setSalesrepname(String salesrepname) {
		this.salesrepname = salesrepname;
	}

	@Generated
	public String getSalesrepname() {
		return salesrepname;
	}

	@Generated
	public String getRowId() {
		return rowid;
	}

	@Override
	public String toString() {
		return  " Order  [  " +  " rowid =" + rowid + "\n" +  
	    ", rowstate =" + rowstate + "\n" + 
	   ", sortorder =" + sortorder + "\n" + 
	   ", billtoid =" + billtoid + "\n" + 
	   ", carrier =" + carrier + "\n" + 
	   ", creditcard =" + creditcard + "\n" + 
	   ", custnum =" + custnum + "\n" + 
	   ", instructions =" + instructions + "\n" + 
	   ", orderdate =" + orderdate + "\n" + 
	   ", ordernum =" + ordernum + "\n" + 
	   ", orderstatus =" + orderstatus + "\n" + 
	   ", po =" + po + "\n" + 
	   ", promisedate =" + promisedate + "\n" + 
	   ", salesrep =" + salesrep + "\n" + 
	   ", shipdate =" + shipdate + "\n" + 
	   ", shiptoid =" + shiptoid + "\n" + 
	   ", terms =" + terms + "\n" + 
	   ", warehousenum =" + warehousenum + "\n" + 
	   ", custname =" + custname + "\n" + 
	   ", salesrepname =" + salesrepname + "\n" + 
	  "] " ;
	}
	
}

