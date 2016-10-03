package com.invensense.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CustomObject1")
@XmlAccessorType(XmlAccessType.NONE)
public class Quote extends BaseModel {
	
	@XmlElement(name="Id")
	private String id;
	
	@XmlElement(name="Name")
	private String quoteName;
	
	@XmlElement(name="IndexedPick1")
	private String quoteStatus;
	
	@XmlElement(name="AccountId")
	private String endCustomer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuoteName() {
		return quoteName;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}

	public String getQuoteStatus() {
		return quoteStatus;
	}

	public void setQuoteStatus(String quoteStatus) {
		this.quoteStatus = quoteStatus;
	}
	
	public String getEndCustomer() {
		return endCustomer;
	}

	public void setEndCustomer(String endCustomer) {
		this.endCustomer = endCustomer;
	}

	public String toString() {
		return "[Quote] Id : " + id + " , Quote Name : " + quoteName + " Quote Status : " + quoteStatus;
	}
}
