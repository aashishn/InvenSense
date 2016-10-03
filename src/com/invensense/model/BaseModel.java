package com.invensense.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import com.crmod.model.BaseObject;

@MappedSuperclass
public class BaseModel extends BaseObject implements GenericModel {

	@Transient
	@XmlElement(name="ExternalSystemId")
	private String externalSystemId ;
	
	@Column(name="crmod_created_by")
	@XmlElement(name="CreatedBy")
	public String crmodCreatedBy ;
	
	@Column(name="crmod_created_date")
	@XmlElement(name="CreatedDate")
	public Date  crmodCreatedDate ;
	
	@Column(name="crmod_modified_by")
	@XmlElement(name="ModifiedBy")
	public String crmodModifiedBy ;
	
	@Column(name="crmod_modified_date")
	@XmlElement(name="ModifiedDate")
	public Date crmodModifiedDate ;
	
	@Column(name="sync_step",length=25)
	private String syncStep ;
	
	@Column(name="sync_status",length=25)
	private String syncStatus ;
	
	@Column(name="retry_count",length=2)
	private Integer retryCount ;
	
	@Column(name="error_message",length=2000)
	private String errorMessage ;
	
	public String getExternalSystemId() {
		return externalSystemId;
	}
	
	public void setExternalSystemId(String externalSystemId) {
		this.externalSystemId = externalSystemId;
	}
	
	public String getCrmodCreatedBy() {
		return crmodCreatedBy;
	}
	
	public void setCrmodCreatedBy(String crmodCreatedBy) {
		this.crmodCreatedBy = crmodCreatedBy;
	}
	
	public Date getCrmodCreatedDate() {
		return crmodCreatedDate;
	}
	
	public void setCrmodCreatedDate(Date crmodCreatedDate) {
		this.crmodCreatedDate = crmodCreatedDate;
	}
	
	public String getCrmodModifiedBy() {
		return crmodModifiedBy;
	}
	
	public void setCrmodModifiedBy(String crmodModifiedBy) {
		this.crmodModifiedBy = crmodModifiedBy;
	}
	
	public Date getCrmodModifiedDate() {
		return crmodModifiedDate;
	}
	
	public void setCrmodModifiedDate(Date crmodModifiedDate) {
		this.crmodModifiedDate = crmodModifiedDate;
	}
	
	public String getSyncStep() {
		return syncStep;
	}
	
	public void setSyncStep(String syncStep) {
		this.syncStep = syncStep;
	}
	
	public String getSyncStatus() {
		return syncStatus;
	}
	
	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}
	
	public Integer getRetryCount() {
		return retryCount;
	}
	
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
