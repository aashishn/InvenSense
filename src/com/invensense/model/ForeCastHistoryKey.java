package com.invensense.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class ForeCastHistoryKey implements Serializable {
	
	private String forecastId;
	
	private String lockedMonth;
	
	private String lockedYear;

	public String getForecastId() {
		return forecastId;
	}

	public void setForecastId(String forecastId) {
		this.forecastId = forecastId;
	}

	public String getLockedMonth() {
		return lockedMonth;
	}

	public void setLockedMonth(String lockedMonth) {
		this.lockedMonth = lockedMonth;
	}

	public String getLockedYear() {
		return lockedYear;
	}

	public void setLockedYear(String lockedYear) {
		this.lockedYear = lockedYear;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((forecastId == null) ? 0 : forecastId.hashCode());
		result = prime * result
				+ ((lockedMonth == null) ? 0 : lockedMonth.hashCode());
		result = prime * result
				+ ((lockedYear == null) ? 0 : lockedYear.hashCode());
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
		ForeCastHistoryKey other = (ForeCastHistoryKey) obj;
		if (forecastId == null) {
			if (other.forecastId != null)
				return false;
		} else if (!forecastId.equals(other.forecastId))
			return false;
		if (lockedMonth == null) {
			if (other.lockedMonth != null)
				return false;
		} else if (!lockedMonth.equals(other.lockedMonth))
			return false;
		if (lockedYear == null) {
			if (other.lockedYear != null)
				return false;
		} else if (!lockedYear.equals(other.lockedYear))
			return false;
		return true;
	}
	
}
