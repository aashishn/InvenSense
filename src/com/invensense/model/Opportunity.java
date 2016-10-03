package com.invensense.model;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Opportunity")
@XmlAccessorType(XmlAccessType.NONE)
public class Opportunity extends BaseModel {

	@XmlElement(name="Id")
	private String id ;
	
	@XmlElement(name="OpportunityName")
	private String name ;

	@XmlElement(name="Description")
	private String description ;
	
	@XmlElement(name = "cAnnual_Revenue")
	private BigDecimal annualRevenue;
	
	@XmlElement(name = "IndexedNumber0")
	private BigDecimal totalQuantity;
	
	@XmlElement(name = "stPrimary_Part_")
	private String primaryPart;
	
	@XmlElement(name = "plPrimary_Product_Category")
	private String primaryProductCategory;
	
	@XmlElement(name = "plBU")
	private String businessUnit;
	
	@XmlElement(name = "IndexedLongText0")
	private String projectName;
	
	@XmlElement(name = "IndexedPick0")
	private String marketSegment;
	
	@XmlElement(name = "IndexedPick1")
	private String subSegment;
	
	@XmlElement(name = "AccountId")
	private String accountId;
	
	@XmlElement(name = "AccountName")
	private String endCustomer;
	
	@XmlElement(name = "ltParent_End_Customer")
	private String parentEndCustomer;
	
	@XmlElement(name = "stParent_End_Customer_ID")
	private String parentEndCustomerId;
	
	@XmlElement(name = "Owner")
	private String owner;
	
	@XmlElement(name = "OwnerId")
	private String ownerId;
	
	@Transient
	@XmlElementWrapper(name="ListOfProductRevenue")
	@XmlElement(name="ProductRevenue")
	private List<OpportunityProductRevenue> opportunityProductRevenueList;
	
	@Transient
	@XmlElementWrapper(name="ListOfTeam")
	@XmlElement(name="Team")
	private List<OpportunityTeam> opportunityTeam;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public BigDecimal getAnnualRevenue() {
		return annualRevenue;
	}

	public void setAnnualRevenue(BigDecimal annualRevenue) {
		this.annualRevenue = annualRevenue;
	}

	public BigDecimal getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(BigDecimal totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	public String getPrimaryPart() {
		return primaryPart;
	}

	public void setPrimaryPart(String primaryPart) {
		this.primaryPart = primaryPart;
	}

	public String getPrimaryProductCategory() {
		return primaryProductCategory;
	}

	public void setPrimaryProductCategory(String primaryProductCategory) {
		this.primaryProductCategory = primaryProductCategory;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMarketSegment() {
		return marketSegment;
	}

	public void setMarketSegment(String marketSegment) {
		this.marketSegment = marketSegment;
	}

	public String getSubSegment() {
		return subSegment;
	}

	public void setSubSegment(String subSegment) {
		this.subSegment = subSegment;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getEndCustomer() {
		return endCustomer;
	}

	public void setEndCustomer(String endCustomer) {
		this.endCustomer = endCustomer;
	}

	public String getParentEndCustomer() {
		return parentEndCustomer;
	}

	public void setParentEndCustomer(String parentEndCustomer) {
		this.parentEndCustomer = parentEndCustomer;
	}
	
	public String getParentEndCustomerId() {
		return parentEndCustomerId;
	}

	public void setParentEndCustomerId(String parentEndCustomerId) {
		this.parentEndCustomerId = parentEndCustomerId;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public List<OpportunityProductRevenue> getOpportunityProductRevenueList() {
		return opportunityProductRevenueList;
	}

	public void setOpportunityProductRevenueList(
			List<OpportunityProductRevenue> opportunityProductRevenueList) {
		this.opportunityProductRevenueList = opportunityProductRevenueList;
	}

	public List<OpportunityTeam> getOpportunityTeam() {
		return opportunityTeam;
	}

	public void setOpportunityTeam(List<OpportunityTeam> opportunityTeam) {
		this.opportunityTeam = opportunityTeam;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Opportunity other = (Opportunity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String toString() {
		return "[Opportunity] Id : " + id + ", Name : " + name + " Annual Revenue " + annualRevenue + " Total Quantity : " + totalQuantity + 
				", OpportunityProductRevenueList : " + opportunityProductRevenueList.toString();
	}
}
