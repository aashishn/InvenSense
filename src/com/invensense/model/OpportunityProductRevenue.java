package com.invensense.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ProductRevenue")
@XmlAccessorType(XmlAccessType.NONE)
public class OpportunityProductRevenue extends BaseModel {
	
	@XmlElement(name = "Id")
	private String id;
	
	@XmlElement(name = "ProductName")
	private String productName;
	
	@XmlElement(name = "Quantity")
	private BigDecimal quantity;
	
	@XmlElement(name = "PurchasePrice")
	private BigDecimal asp;
	
	@XmlElement(name = "Revenue")
	private BigDecimal revenue;
	
	@XmlElement(name = "Status")
	private String status;
	
	@XmlElement(name = "IndexedBoolean0")
	private Boolean primary;
	
	@XmlElement(name = "ProductCategory")
	private String productCategory;
	
	@XmlElement(name = "OpportunityId")
	private String opportunityId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAsp() {
		return asp;
	}

	public void setAsp(BigDecimal asp) {
		this.asp = asp;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}
	
	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	
	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String toString() {
		return "[Opportunity Product Revenue] Id : " + id + ", Product Name : " + productName + " , ASP : " + asp + " , Quantity : " + 
				quantity + " , Revenue : " + revenue + " , Status : " + status + " , Primary : " + primary +  " , Product Category : " + productCategory + " , Modified Date : " + getCrmodModifiedDate();
	}

}
