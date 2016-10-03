package com.invensense.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SalesForecastLockInfoKey implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String month;
	private String year;
	private String forecastType;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getForecastType() {
		return forecastType;
	}

	public void setForecastType(String forecastType) {
		this.forecastType = forecastType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((forecastType == null) ? 0 : forecastType.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
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
		SalesForecastLockInfoKey other = (SalesForecastLockInfoKey) obj;
		if (forecastType == null) {
			if (other.forecastType != null)
				return false;
		} else if (!forecastType.equals(other.forecastType))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
	
}
