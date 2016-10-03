package com.invensense.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name="SALES_FORECAST_LOCK_INFO")
@IdClass(SalesForecastLockInfoKey.class)
public class SalesForecastLockInfo implements GenericModel{
	
	
	@Id
	@Column(name = "month")
	private String month;
	
	@Id
	@Column(name = "year", length = 4)
	private String year;
	
	private Integer monthSequence;
	
	@Column(name = "locked")
	private boolean locked;
	
	@Column(name = "lockedBy")
	private String lockedBy;
	
	@Column(name = "lockedDate")
	private Date lockedDate;
	
	@Id
	@Column(name = "forecastType")
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
	
	public Integer getMonthSequence() {
		return monthSequence;
	}

	public void setMonthSequence(Integer monthSequence) {
		this.monthSequence = monthSequence;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public String getForecastType() {
		return forecastType;
	}

	public void setForecastType(String forecastType) {
		this.forecastType = forecastType;
	}
}
