package com.invensense.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="job")
public class Job implements GenericModel {

	@Id
	private String jobName ;
	
	@Column
	private String description ;
	
	@Column
	private boolean active ;
	
	@Column
	private String status ;
	
	@Column(name="last_run")
	private Date lastRun ;
	
	@Column(name="error_log",length=6000)
	private String errorLog ;
	
	@Column
	private boolean enableNotification ;
	
	private Date startDate;
	
	private Date endDate;
	
	private String duration;
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getLastRun() {
		return lastRun;
	}
	
	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}
	
	public String getErrorLog() {
		return errorLog;
	}
	
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	
	public boolean isEnableNotification() {
		return enableNotification;
	}
	
	public void setEnableNotification(boolean enableNotification) {
		this.enableNotification = enableNotification;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}
