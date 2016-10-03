package com.invensense.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name="FINANCE_FORECAST_EXIST")
@IdClass(FinanceForecastExistKey.class)
public class FinanceForecastExist implements GenericModel {
	
	@Id
	@Column(name = "month")
	private String month;
	
	@Id
	@Column(name = "year", length = 4)
	private String year;
	
	private Integer monthSequence;
	
	Boolean recordExist;
	
	Boolean locked;

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

	public Boolean getRecordExist() {
		return recordExist;
	}

	public void setRecordExist(Boolean recordExist) {
		this.recordExist = recordExist;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
}
