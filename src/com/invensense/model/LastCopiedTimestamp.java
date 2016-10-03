package com.invensense.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "LAST_COPIED_TIMESTAMP")
public class LastCopiedTimestamp {
	
	@Id
	private String forecastType;
	
	private Date lastCopiedTimestamp;
	
	private String lastCopiedTimestampString;

	public String getForecastType() {
		return forecastType;
	}

	public void setForecastType(String forecastType) {
		this.forecastType = forecastType;
	}

	public Date getLastCopiedTimestamp() {
		return lastCopiedTimestamp;
	}

	public void setLastCopiedTimestamp(Date lastCopiedTimestamp) {
		this.lastCopiedTimestamp = lastCopiedTimestamp;
	}

	public String getLastCopiedTimestampString() {
		return lastCopiedTimestampString;
	}

	public void setLastCopiedTimestampString(String lastCopiedTimestampString) {
		this.lastCopiedTimestampString = lastCopiedTimestampString;
	}

}
