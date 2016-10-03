package com.invensense.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.crmod.model.BaseObject;

@XmlRootElement(name="Team")
@XmlAccessorType(XmlAccessType.NONE)
public class QuoteLineItemTeamWS1 extends BaseObject {
	
	@XmlElement(name="CustomObject2TeamId")
	private String id;
	
	@XmlElement(name="UserFirstName")
	private String firstName;
	
	@XmlElement(name="UserLastName")
	private String lastName;
	
	@XmlElement(name="TeamRole")
	private String teamRole;
	
	private String userRole;
	
	@XmlElement(name = "CustomObject2AccessId")
	private String accessId;
	
	@XmlElement(name = "CustomObject2Access")
	private String access;
	
	@XmlElement(name = "UserId")
	private String userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
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

	public String toString() {
		return "[Quote Line Item Team] Id : " + id + " , First Name : " + firstName + " , Last Name : " + lastName + 
				" , Team Role : " + teamRole + ", Access Id : " + accessId + " Access Row Id : " + access;
	}

}
