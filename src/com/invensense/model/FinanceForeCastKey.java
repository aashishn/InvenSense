package com.invensense.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class FinanceForeCastKey implements Serializable {
	
	private String endCustomerId;
	
	private String productId;
	
	private String year;
	
	private String market;
	
	private String subMarket;
	
	private String businessUnit;
	
	private String programName;
	
	private String month;
	
	public String getEndCustomerId() {
		return endCustomerId;
	}

	public void setEndCustomerId(String endCustomerId) {
		this.endCustomerId = endCustomerId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getSubMarket() {
		return subMarket;
	}

	public void setSubMarket(String subMarket) {
		this.subMarket = subMarket;
	}
	
	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((businessUnit == null) ? 0 : businessUnit.hashCode());
		result = prime * result
				+ ((endCustomerId == null) ? 0 : endCustomerId.hashCode());
		result = prime * result + ((market == null) ? 0 : market.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((programName == null) ? 0 : programName.hashCode());
		result = prime * result
				+ ((subMarket == null) ? 0 : subMarket.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		FinanceForeCastKey other = (FinanceForeCastKey) obj;
		if (businessUnit == null) {
			if (other.businessUnit != null)
				return false;
		} else if (!businessUnit.equals(other.businessUnit))
			return false;
		if (endCustomerId == null) {
			if (other.endCustomerId != null)
				return false;
		} else if (!endCustomerId.equals(other.endCustomerId))
			return false;
		if (market == null) {
			if (other.market != null)
				return false;
		} else if (!market.equals(other.market))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (programName == null) {
			if (other.programName != null)
				return false;
		} else if (!programName.equals(other.programName))
			return false;
		if (subMarket == null) {
			if (other.subMarket != null)
				return false;
		} else if (!subMarket.equals(other.subMarket))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

}
