package com.invensense.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Account")
@XmlAccessorType(XmlAccessType.NONE)
@Entity(name="ACCOUNT")
public class Account extends BaseModel {

	@Id
	@XmlElement(name="Id")
	private String id ;

	@Column(name="account_name")
	@XmlElement(name="AccountName")
	private String name ;

	@Column(name="account_type")
	@XmlElement(name="AccountType")
	private String accountType ;

	@Column(name="owner")
	@XmlElement(name="Owner")
	private String owner ;
	
	@Column(name="owner_id")
	@XmlElement(name="OwnerId")
	private String ownerId ;
	
	@XmlElement(name = "ParentAccountId")
	private String parentAccountId;
	
	@XmlElement(name = "ParentAccount")
	private String parentAccountName;
	
	@Transient
	@XmlElementWrapper(name="ListOfTeam")
	@XmlElement(name="Team")
	private List<AccountTeam> accountTeamList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getParentAccountId() {
		return parentAccountId;
	}

	public void setParentAccountId(String parentAccountId) {
		this.parentAccountId = parentAccountId;
	}
	
	public String getParentAccountName() {
		return parentAccountName;
	}

	public void setParentAccountName(String parentAccountName) {
		this.parentAccountName = parentAccountName;
	}

	public List<AccountTeam> getAccountTeamList() {
		return accountTeamList;
	}

	public void setAccountTeamList(List<AccountTeam> accountTeamList) {
		this.accountTeamList = accountTeamList;
	}

	@Override
	public String toString() {
		return "[Account] Id : " + id + " , Name : " + name + " , Type : " + accountType + " , Account Team List : " + accountTeamList.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
