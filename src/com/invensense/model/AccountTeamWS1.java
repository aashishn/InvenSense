package com.invensense.model;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.crmod.model.BaseObject;

@XmlRootElement(name="AccountTeamWS1")
@XmlAccessorType(XmlAccessType.NONE)
@Entity(name = "ACCOUNT_TEAM")
@Access(AccessType.FIELD)
public class AccountTeamWS1 extends BaseObject {
	
	@Id
	@XmlElement(name="AccountTeamId")
	private String id;
	
	@XmlElement(name="AccountId")
	private String accountId;
	
	@XmlElement(name="FirstName")
	private String firstName;
	
	@XmlElement(name="LastName")
	private String lastName;
	
	@XmlElement(name="TeamRole")
	private String teamRole;
	
	@XmlElement(name = "RoleName")
	private String userRole;
	
	@XmlElement(name = "AccountAccess")
	private String access;
	
	@XmlElement(name = "UserId")
	private String userId;
	
	@XmlElement(name = "UserAlias")
	private String userAlias;
	
	@XmlElement(name = "AccountAccessId")
	private String accessId;
	
	private Date crmodCreatedDate;
	
	private Date crmodModifiedDate;
	
	@Transient
	@XmlElement(name = "CreatedDate")
	private String createdDate;
	
	@Transient
	@XmlElement(name = "ModifiedDate")
	private String modifiedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTeamRole() {
		return teamRole;
	}

	public void setTeamRole(String teamRole) {
		this.teamRole = teamRole;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public Date getCrmodCreatedDate() {
		return crmodCreatedDate;
	}

	public void setCrmodCreatedDate(Date crmodCreatedDate) {
		this.crmodCreatedDate = crmodCreatedDate;
	}

	public Date getCrmodModifiedDate() {
		return crmodModifiedDate;
	}

	public void setCrmodModifiedDate(Date crmodModifiedDate) {
		this.crmodModifiedDate = crmodModifiedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String toString() {
		return "[Account Team] Id : " + id + " , Account Id : " + accountId + " , First Name : " + firstName + " , " + " , User Id " + userId + 
				"Last Name : " + lastName + " , Team Role : " + teamRole + " , User Role : " + userRole + " , Account Access : " + access + 
				" , Created Date : " + createdDate + " , Modified Date : " + modifiedDate;
	}

}
