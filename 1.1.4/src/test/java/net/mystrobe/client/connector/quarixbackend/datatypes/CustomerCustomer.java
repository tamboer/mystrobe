/**
 * Copyright (C) 2010-2011 TVH Group NV. <kalman.tiboldi@tvh.com>
 *
 * This file is part of the MyStroBe project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package net.mystrobe.client.connector.quarixbackend.datatypes;

import java.math.BigDecimal;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataSource;
import net.mystrobe.client.connector.quarixbackend.Generated;
import net.mystrobe.client.connector.quarixbackend.GeneratorMeta;

/**
 * 
 * This class was generated by net.mystrobe.client.connector.quarixbackend.ClassGenerator
 * 
 * Used parameters: appName: wicketds dsName: server.customer doName: tt-customer
 * 
 */

@GeneratorMeta(appName="wicketds", urn="server.customer", dsName="server.customer", dsId="dsCustomer", daoId="tt-customer", isLocked=false)
public class CustomerCustomer  implements IDataBean {

	@Generated
	private static final long serialVersionUID = 1308428577767L;

	@Generated
	protected IDataSource dataSource;

	@Generated
	protected String rowid;
	@Generated
	protected Integer rowstate;
	@Generated
	protected Integer custnum;
	@Generated
	protected String country;
	@Generated
	protected String name;
	@Generated
	protected String address;
	@Generated
	protected String address2;
	@Generated
	protected String city;
	@Generated
	protected String state;
	@Generated
	protected String postalcode;
	@Generated
	protected String contact;
	@Generated
	protected String phone;
	@Generated
	protected String salesrep;
	@Generated
	protected BigDecimal creditlimit;
	@Generated
	protected BigDecimal balance;
	@Generated
	protected String terms;
	@Generated
	protected Integer discount;
	@Generated
	protected String comments;

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
	public void setCustnum(Integer custnum) {
		this.custnum = custnum;
	}

	@Generated
	public Integer getCustnum() {
		return custnum;
	}

	@Generated
	public void setCountry(String country) {
		this.country = country;
	}

	@Generated
	public String getCountry() {
		return country;
	}

	@Generated
	public void setName(String name) {
		this.name = name;
	}

	@Generated
	public String getName() {
		return name;
	}

	@Generated
	public void setAddress(String address) {
		this.address = address;
	}

	@Generated
	public String getAddress() {
		return address;
	}

	@Generated
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Generated
	public String getAddress2() {
		return address2;
	}

	@Generated
	public void setCity(String city) {
		this.city = city;
	}

	@Generated
	public String getCity() {
		return city;
	}

	@Generated
	public void setState(String state) {
		this.state = state;
	}

	@Generated
	public String getState() {
		return state;
	}

	@Generated
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	@Generated
	public String getPostalcode() {
		return postalcode;
	}

	@Generated
	public void setContact(String contact) {
		this.contact = contact;
	}

	@Generated
	public String getContact() {
		return contact;
	}

	@Generated
	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Generated
	public String getPhone() {
		return phone;
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
	public void setCreditlimit(BigDecimal creditlimit) {
		this.creditlimit = creditlimit;
	}

	@Generated
	public BigDecimal getCreditlimit() {
		return creditlimit;
	}

	@Generated
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Generated
	public BigDecimal getBalance() {
		return balance;
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
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	@Generated
	public Integer getDiscount() {
		return discount;
	}

	@Generated
	public void setComments(String comments) {
		this.comments = comments;
	}

	@Generated
	public String getComments() {
		return comments;
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
	
	
}

