package net.quarix.qrx4j.samples.data.beans;

import java.math.BigDecimal;
import net.mystrobe.client.IDataSource;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;
import net.mystrobe.client.connector.quarixbackend.Generated;

/**
 *
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 *
 * Used parameters:
 *		appName: qrxexmpl
 *		 dsName: wicketds.server.dtordordline
 *		 doName: ttorder
 *
 */

@GeneratorMeta(appName="qrxexmpl", urn="wicketds.server.dtordordline", dsName="wicketds.server.dtordordline", dsId="daordordline", daoId="ttorderline", isLocked=false)
public class OrderLine  implements IDataBean {

	@Generated
	private static final long serialVersionUID = 1359137283123L;

	@Generated
	protected IDataSource dataSource;

	@Generated
	protected String rowid;
	@Generated
	protected Integer rowstate;
	@Generated
	protected Integer sortorder;
	@Generated
	protected Integer discount;
	@Generated
	protected BigDecimal extendedprice;
	@Generated
	protected Integer itemnum;
	@Generated
	protected Integer linenum;
	@Generated
	protected String orderlinestatus;
	@Generated
	protected Integer ordernum;
	@Generated
	protected BigDecimal price;
	@Generated
	protected Integer qty;
	@Generated
	protected String itemname;

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
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	@Generated
	public Integer getDiscount() {
		return discount;
	}

	@Generated
	public void setExtendedprice(BigDecimal extendedprice) {
		this.extendedprice = extendedprice;
	}

	@Generated
	public BigDecimal getExtendedprice() {
		return extendedprice;
	}

	@Generated
	public void setItemnum(Integer itemnum) {
		this.itemnum = itemnum;
	}

	@Generated
	public Integer getItemnum() {
		return itemnum;
	}

	@Generated
	public void setLinenum(Integer linenum) {
		this.linenum = linenum;
	}

	@Generated
	public Integer getLinenum() {
		return linenum;
	}

	@Generated
	public void setOrderlinestatus(String orderlinestatus) {
		this.orderlinestatus = orderlinestatus;
	}

	@Generated
	public String getOrderlinestatus() {
		return orderlinestatus;
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
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Generated
	public BigDecimal getPrice() {
		return price;
	}

	@Generated
	public void setQty(Integer qty) {
		this.qty = qty;
	}

	@Generated
	public Integer getQty() {
		return qty;
	}

	@Generated
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	@Generated
	public String getItemname() {
		return itemname;
	}

	@Generated
	public IDataSource getDataSource() {
		return dataSource;
	}

	@Generated
	public String getRowId() {
		return rowid;
	}

	@Generated
	public void detach() {		
	}

	@Override
	public String toString() {
		return  " OrderLine  [  " +  " rowid =" + rowid + "\n" +  
	    ", rowstate =" + rowstate + "\n" + 
	   ", sortorder =" + sortorder + "\n" + 
	   ", discount =" + discount + "\n" + 
	   ", extendedprice =" + extendedprice + "\n" + 
	   ", itemnum =" + itemnum + "\n" + 
	   ", linenum =" + linenum + "\n" + 
	   ", orderlinestatus =" + orderlinestatus + "\n" + 
	   ", ordernum =" + ordernum + "\n" + 
	   ", price =" + price + "\n" + 
	   ", qty =" + qty + "\n" + 
	   ", itemname =" + itemname + "\n" + 
	  "] " ;
	}
	
	
}

