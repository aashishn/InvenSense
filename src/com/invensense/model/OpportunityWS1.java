package com.invensense.model;

import java.util.List;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Opportunity")
@XmlAccessorType(XmlAccessType.NONE)
public class OpportunityWS1 extends BaseModel {
	
	@XmlElement(name="OpportunityId")
	private String id ;
	
	@Transient
	@XmlElementWrapper(name="ListOfTeam")
	@XmlElement(name="Team")
	private List<OpportunityTeamWS1> opportunityTeam;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<OpportunityTeamWS1> getOpportunityTeam() {
		return opportunityTeam;
	}

	public void setOpportunityTeam(List<OpportunityTeamWS1> opportunityTeam) {
		this.opportunityTeam = opportunityTeam;
	}
}
