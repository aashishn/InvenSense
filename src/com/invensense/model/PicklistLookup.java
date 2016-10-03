package com.invensense.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "PICKLIST_LOOKUP")
public class PicklistLookup implements Comparable<PicklistLookup> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String lookupName;
	
	private String lookupValue;
	
	private String parentLookupName;
	
	private String parentLookupValue;
	
	@Column(name = "active", columnDefinition = "bit")
	private Boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getLookupValue() {
		return lookupValue;
	}

	public void setLookupValue(String lookupValue) {
		this.lookupValue = lookupValue;
	}

	public String getParentLookupName() {
		return parentLookupName;
	}

	public void setParentLookupName(String parentLookupName) {
		this.parentLookupName = parentLookupName;
	}

	public String getParentLookupValue() {
		return parentLookupValue;
	}

	public void setParentLookupValue(String parentLookupValue) {
		this.parentLookupValue = parentLookupValue;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lookupValue == null) ? 0 : lookupValue.hashCode());
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
		PicklistLookup other = (PicklistLookup) obj;
		if (lookupValue == null) {
			if (other.lookupValue != null)
				return false;
		} else if (!lookupValue.equals(other.lookupValue))
			return false;
		return true;
	}

	@Override
	public int compareTo(PicklistLookup picklistLookup) {
		return lookupValue.compareTo(picklistLookup.getLookupValue());
	}

}
