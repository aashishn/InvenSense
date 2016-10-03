package com.invensense.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="system_property")
public class SystemProperty  implements GenericModel {

	@Id
	private String keyName ;

	@Column
	private String picklistName;

	@Column
	private String keyValue;
	
	@Column
	private String description ;
	
	@Column
	private boolean fieldStatus = true;

	@Column
	private int fieldOrder = 0;
	

	public String getPicklistName() {
		return picklistName;
	}

	public void setPicklistName(String picklistName) {
		this.picklistName = picklistName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFieldStatus() {
		return fieldStatus;
	}

	public void setFieldStatus(boolean fieldStatus) {
		this.fieldStatus = fieldStatus;
	}

	public int getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(int fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	
}

