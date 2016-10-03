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
public class QuoteLineItemWS1 extends BaseModel {
	
	@XmlElement(name="CustomObject2Id")
	private String id;
	
	@XmlElement(name="Name")
	private String quoteLineItemName;
	
	//@XmlElement(name="AccountId")
	private String endCustomer;
	
	@Transient
	@XmlElementWrapper(name="ListOfTeam")
	@XmlElement(name="Team")
	private List<QuoteLineItemTeamWS1> quoteLineItemTeamList;
	
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

	public String getEndCustomer() {
		return endCustomer;
	}

	public void setEndCustomer(String endCustomer) {
		this.endCustomer = endCustomer;
	}

	public List<QuoteLineItemTeamWS1> getQuoteLineItemTeamList() {
		return quoteLineItemTeamList;
	}

	public void setQuoteLineItemTeamList(
			List<QuoteLineItemTeamWS1> quoteLineItemTeamList) {
		this.quoteLineItemTeamList = quoteLineItemTeamList;
	}

	public String toString() {
		return "[QuoteLineItem] Id : " + id + " , QuoteLineItemName : " + quoteLineItemName + " , End Customer :  " + endCustomer + " , Quote Line Item Team List : " + quoteLineItemTeamList.toString();
	}
}
