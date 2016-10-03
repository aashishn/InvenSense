package com.invensense.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="ERRORLOG")
public class ErrorLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String tableName;
	
	private int severity;
	
	private String emailSent;
	
	private String direction;
	
	private Timestamp errorTimestamp;
	
	@Column(name = "Error_Message",length=6000)
	private String errorMessage;
	
	private String primaryFieldName;
	
	private String primaryFieldValue;
	
	@Column(columnDefinition="text")
	private String data;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getEmailSent() {
		return emailSent;
	}

	public void setEmailSent(String emailSent) {
		this.emailSent = emailSent;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Timestamp getErrorTimestamp() {
		return errorTimestamp;
	}

	public void setErrorTimestamp(Timestamp errorTimestamp) {
		this.errorTimestamp = errorTimestamp;
	}

	public String getPrimaryFieldName() {
		return primaryFieldName;
	}

	public void setPrimaryFieldName(String primaryFieldName) {
		this.primaryFieldName = primaryFieldName;
	}

	public String getPrimaryFieldValue() {
		return primaryFieldValue;
	}

	public void setPrimaryFieldValue(String primaryFieldValue) {
		this.primaryFieldValue = primaryFieldValue;
	}
}
