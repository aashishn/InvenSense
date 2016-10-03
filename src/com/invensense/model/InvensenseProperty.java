package com.invensense.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "INVENSENSE_PROPERTY")
public class InvensenseProperty {
	
	@Id
	private String name;
	
	private String value;
	
	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
