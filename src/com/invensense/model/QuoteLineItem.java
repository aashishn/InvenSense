package com.invensense.model;

import java.util.List;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CustomObject2")
@XmlAccessorType(XmlAccessType.NONE)
public class QuoteLineItem extends BaseModel {
	
	@XmlElement(name="Id")
	private String id;
	
	@XmlElement(name="Name")
	private String quoteLineItemName;
	
	@XmlElement(name="CustomObject1Id")
	private String quoteId;
	
	@XmlElement(name="IndexedPick4")
	private String approvalStatus;
	
	@XmlElement(name="AccountId")
	private String endCustomer;
	
	// OJDM - 'Add Product BU Manager' to QLI
	@Transient
	@XmlElement(name = "IndexedPick0")
	private String marketSegment;

	@Transient
	@XmlElementWrapper(name="ListOfTeam")
	@XmlElement(name="Team")
	private List<QuoteLineItemTeam> quoteLineItemTeam;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuoteLineItemName() {
		return quoteLineItemName;
	}

	public void setQuoteLineItemName(String quoteLineItemName) {
		this.quoteLineItemName = quoteLineItemName;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	public String getEndCustomer() {
		return endCustomer;
	}

	public void setEndCustomer(String endCustomer) {
		this.endCustomer = endCustomer;
	}
	
	public String getMarketSegment() {
		return marketSegment;
	}

	public void setMarketSegment(String marketSegment) {
		this.marketSegment = marketSegment;
	}
	
	public List<QuoteLineItemTeam> getQuoteLineItemTeam() {
		return quoteLineItemTeam;
	}

	public void setQuoteLineItemTeam(List<QuoteLineItemTeam> quoteLineItemTeam) {
		this.quoteLineItemTeam = quoteLineItemTeam;
	}

	public String toString() {
		return "[QuoteLineItem] Id : " + id + " , QuoteLineItemName : " + quoteLineItemName + " , QuoteId : " + quoteId + " , Approval Status : " + approvalStatus;
	}
}
