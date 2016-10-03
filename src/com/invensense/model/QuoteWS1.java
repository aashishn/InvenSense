package com.invensense.model;

import java.util.List;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CustomObject1")
@XmlAccessorType(XmlAccessType.NONE)
public class QuoteWS1 extends BaseModel {
	
	@XmlElement(name="CustomObject1Id")
	private String id;
	
	@XmlElement(name="Name")
	private String quoteName;
	
	@XmlElement(name="IndexedPick1")
	private String quoteStatus;
	
	@XmlElement(name="AccountId")
	private String endCustomer;
	
	@Transient
	@XmlElementWrapper(name="ListOfCustomObject2")
	@XmlElement(name="CustomObject2")
	private List<QuoteLineItemWS1> quoteLineItemList;
	
	@Transient
	@XmlElementWrapper(name="ListOfTeam")
	@XmlElement(name="Team")
	private List<QuoteTeamWS1> quoteTeamList;

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

	public List<QuoteLineItemWS1> getQuoteLineItemList() {
		return quoteLineItemList;
	}

	public void setQuoteLineItemList(List<QuoteLineItemWS1> quoteLineItemList) {
		this.quoteLineItemList = quoteLineItemList;
	}
	
	public List<QuoteTeamWS1> getQuoteTeamList() {
		return quoteTeamList;
	}

	public void setQuoteTeamList(List<QuoteTeamWS1> quoteTeamList) {
		this.quoteTeamList = quoteTeamList;
	}

	public String toString() {
		return "[Quote] Id : " + id + " , Quote Name : " + quoteName + " Quote Status : " + quoteStatus + " End Customer : " + endCustomer + 
				" , Quote Line Item List : " + quoteLineItemList.toString() + " , Quote Team List : " + quoteTeamList.toString();
	}
}
