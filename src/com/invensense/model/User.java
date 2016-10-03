package com.invensense.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="User")
@XmlAccessorType(XmlAccessType.NONE)
@Entity(name="USERS")
public class User extends BaseModel {
	
	@Id
	@XmlElement(name="Id")
	private String id;
	
	@XmlElement(name="FirstName")
	@Column(name="firstName")
	private String firstName;
	
	// OJDM - User Sync - additional fields - Start
	//Middle Name
	@XmlElement(name="MiddleName")
	private String middleName;
	// OJDM - User Sync - additional fields - End
	
	@XmlElement(name="LastName")
	@Column(name="lastName")
	private String lastName;
	
	@XmlElement(name="UserLoginId")
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@XmlElement(name="Status")
	@Column(name="status")
	private String status;
	
	@XmlElement(name="Role")
	@Column(name="crmodRole")
	private String crmodRole ;
	
	@XmlElement(name="ManagerId")
	@Column(name="reportsTo")
	private String reportsTo ;
	
	// OJDM - User Sync - additional fields - Start
	// Reports To
	@XmlElement(name="ManagerFullName")
	private String reportsToFullName ;
	
	@XmlElement(name="ManagerAlias")
	private String reportsToAlias ;
	// OJDM - User Sync - additional fields - End
	
	@XmlElement(name="Alias")
	@Column(name="userAlias")
	private String userAlias ;
	
	@XmlElement(name = "UserSignInId")
	private String userSignInId;
	
	@XmlElement(name="bCan_Lock_Sales_Forecast")
	@Column(name="canLockSalesForecast")
	private String canLockSalesForecast ;
	
	@Column(name="enabled")
	private boolean enabled ;		
	
	@Transient
	private String breadcrumName  = "";
	
	@Transient
	private int hierarchyLevel ;
	
	@Transient
	private Set<User> salesRepList ;

	// OJDM - User Sync - additional fields - Start
	
	//Job Title
	@XmlElement(name="JobTitle")
	private String jobTitle;
	
	//Territory
	//CustomPickList0
	@XmlElement(name="plTerritory")
	private String territory;
	
	//Email
	@XmlElement(name="EMailAddr")
	private String email;
	
	// Market
	@XmlElement(name="Market")
	private String market ;
	
	// SubMarket
	@XmlElement(name="SubMarket")
	private String subMarket ;
	
	//External Unique ID
	@XmlElement(name="ExternalSystemId")
	private String externalUniqueId;
	
	// OJDM - User Sync - additional fields - End
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getCrmodRole() {
		return crmodRole;
	}

	public void setCrmodRole(String crmodRole) {
		this.crmodRole = crmodRole;
	}

	public String getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getBreadcrumName() {
		return breadcrumName;
	}

	public void setBreadcrumName(String breadcrumName) {
		this.breadcrumName = breadcrumName;
	}

	public int getHierarchyLevel() {
		return hierarchyLevel;
	}

	public void setHierarchyLevel(int hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}

	public String getUserAlias() {
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getUserSignInId() {
		return userSignInId;
	}

	public void setUserSignInId(String userSignInId) {
		this.userSignInId = userSignInId;
	}

	public Set<User> getSalesRepList() {
		return salesRepList;
	}

	public void setSalesRepList(Set<User> salesRepList) {
		this.salesRepList = salesRepList;
	}

	public String getCanLockSalesForecast() {
		return canLockSalesForecast;
	}

	public void setCanLockSalesForecast(String canLockSalesForecast) {
		this.canLockSalesForecast = canLockSalesForecast;
	}

	public String getFullName() {
    	return (getFirstName() != null ? getFirstName() : "" )
		+ " "
		+ (getLastName() != null ? getLastName() : "");
    }

	// OJDM - User Sync - additional fields - Start
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getSubMarket() {
		return subMarket;
	}

	public void setSubMarket(String subMarket) {
		this.subMarket = subMarket;
	}

	public String getReportsToFullName() {
		return reportsToFullName;
	}

	public void setReportsToFullName(String reportsToFullName) {
		this.reportsToFullName = reportsToFullName;
	}

	public String getReportsToAlias() {
		return reportsToAlias;
	}

	public void setReportsToAlias(String reportsToAlias) {
		this.reportsToAlias = reportsToAlias;
	}

	public String getExternalUniqueId() {
		return externalUniqueId;
	}

	public void setExternalUniqueId(String externalUniqueId) {
		this.externalUniqueId = externalUniqueId;
	}
	
	// OJDM - User Sync - additional fields - End
	
	@Override
	public String toString() {
		return "User [userId=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + ", password=" + password + ", status=" + status
				+ ", crmodRole=" + crmodRole + ", enabled=" + enabled + "]";
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
