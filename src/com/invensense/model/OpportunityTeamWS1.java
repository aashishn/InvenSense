package com.invensense.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.crmod.model.BaseObject;

@XmlRootElement(name="Team")
@XmlAccessorType(XmlAccessType.NONE)
public class OpportunityTeamWS1 extends BaseObject {
	
	@XmlElement(name="OpportunityTeamId")
	private String id;
	
	@XmlElement(name = "UserId")
	private String userId;
	
	@XmlElement(name="UserFirstName")
	private String firstName;
	
	@XmlElement(name="UserLastName")
	private String lastName;
	
	@XmlElement(name="TeamRole")
	private String teamRole;
	
	@XmlElement(name = "OpportunityAccess")
	private String access;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
}
