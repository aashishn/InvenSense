package com.invensense.model;

import java.util.List;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Account")
@XmlAccessorType(XmlAccessType.NONE)
public class AccountWS1 extends BaseModel {

	@XmlElement(name="AccountId")
	private String id ;

	@XmlElementWrapper(name="ListOfAccountTeam")
	@XmlElement(name="AccountTeam")
	private List<AccountTeamWS1> accountTeamWS1;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<AccountTeamWS1> getAccountTeamWS1() {
		return accountTeamWS1;
	}

	public void setAccountTeamWS1(List<AccountTeamWS1> accountTeamWS1) {
		this.accountTeamWS1 = accountTeamWS1;
	}

}
