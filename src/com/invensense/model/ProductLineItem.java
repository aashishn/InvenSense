package com.invensense.model;

import java.util.Date;

public class ProductLineItem implements Comparable<ProductLineItem> {
	
	private String id;
	
	private Date modifiedDate;
	
	private OpportunityProductRevenue opportunityProductRevenue;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public OpportunityProductRevenue getOpportunityProductRevenue() {
		return opportunityProductRevenue;
	}

	public void setOpportunityProductRevenue(
			OpportunityProductRevenue opportunityProductRevenue) {
		this.opportunityProductRevenue = opportunityProductRevenue;
	}

	@Override
	public int compareTo(ProductLineItem o) {
		return this.modifiedDate.compareTo(o.getModifiedDate());
	}
	
	public String toString() {
		return "[Product Line Item] Id : " + id + " , Modified Date : " + modifiedDate + " , Product Revnue : " + opportunityProductRevenue.toString();
	}

}
