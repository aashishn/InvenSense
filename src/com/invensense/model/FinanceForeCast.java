package com.invensense.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.crmod.model.BaseObject;

@Entity(name="FINANCE_FORECAST")
@IdClass(FinanceForeCastKey.class)
public class FinanceForeCast extends BaseObject {
		
	@Id
	@Column(name = "end_customer_id", length = 15)
	private String endCustomerId;
	
	@Id
	@Column(name = "product_id", length = 15)
	private String productId;
	
	@Id
	@Column(name = "year", length = 4)
	private String year;
	
	@Id
	@Column(name = "market", length = 50)
	private String market;
	
	@Id
	@Column(name = "sub_market", length = 50)
	private String subMarket;
	
	@Id
	@Column(name = "business_unit", length = 50)
	private String businessUnit;
	
	@Id
	private String programName;
	
	@Id
	@Column(name = "month", length = 15)
	private String month;
	
	@Column(name = "part_number")
	private String partNumber;
	
	private String productName;
	
	private String salesForecastRowId;
	
	private String name;
	
	@Column(name = "asp", columnDefinition = "decimal(19,3)")
	private BigDecimal asp;
	
	@Column(name = "quantity", columnDefinition = "decimal(19,3)")
	private BigDecimal quantity;
	
	private BigDecimal revenue;
	
	private String forecastType;
	
	private String role;
	
	private String parentEndCustomer;
	
	private String parentEndCustomerName;
	
	private String productCategory;

	private String owner;
	
	private String salesRepId;
	
	@Column(name = "asp1", columnDefinition = "decimal(19,3)")
	private BigDecimal asp1;
	
	@Column(name = "asp2", columnDefinition = "decimal(19,3)")
	private BigDecimal asp2;
	
	@Column(name = "asp3", columnDefinition = "decimal(19,3)")
	private BigDecimal asp3;
	
	@Column(name = "asp4", columnDefinition = "decimal(19,3)")
	private BigDecimal asp4;
	
	@Column(name = "asp5", columnDefinition = "decimal(19,3)")
	private BigDecimal asp5;
	
	@Column(name = "asp6", columnDefinition = "decimal(19,3)")
	private BigDecimal asp6;
	
	@Column(name = "asp7", columnDefinition = "decimal(19,3)")
	private BigDecimal asp7;
	
	@Column(name = "asp8", columnDefinition = "decimal(19,3)")
	private BigDecimal asp8;
	
	@Column(name = "asp9", columnDefinition = "decimal(19,3)")
	private BigDecimal asp9;
	
	@Column(name = "asp10", columnDefinition = "decimal(19,3)")
	private BigDecimal asp10;
	
	@Column(name = "asp11", columnDefinition = "decimal(19,3)")
	private BigDecimal asp11;
	
	@Column(name = "asp12", columnDefinition = "decimal(19,3)")
	private BigDecimal asp12;
	
	private Integer quantity1;
	
	private Integer quantity2;
	
	private Integer quantity3;
	
	private Integer quantity4;
	
	private Integer quantity5;
	
	private Integer quantity6;
	
	private Integer quantity7;
	
	private Integer quantity8;
	
	private Integer quantity9;
	
	private Integer quantity10;
	
	private Integer quantity11;
	
	private Integer quantity12;
	
	@Column(name="created_by",length=255)
	private String createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="modified_by",length=255)
	private String modifiedBy;
	
	@Column(name="modified_date")
	private Date modifiedDate;

	public String getSalesForecastRowId() {
		return salesForecastRowId;
	}

	public void setSalesForecastRowId(String salesForecastRowId) {
		this.salesForecastRowId = salesForecastRowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getAsp() {
		return asp;
	}

	public void setAsp(BigDecimal asp) {
		this.asp = asp;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	public String getForecastType() {
		return forecastType;
	}

	public void setForecastType(String forecastType) {
		this.forecastType = forecastType;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getParentEndCustomer() {
		return parentEndCustomer;
	}

	public void setParentEndCustomer(String parentEndCustomer) {
		this.parentEndCustomer = parentEndCustomer;
	}
	
	public String getParentEndCustomerName() {
		return parentEndCustomerName;
	}

	public void setParentEndCustomerName(String parentEndCustomerName) {
		this.parentEndCustomerName = parentEndCustomerName;
	}

	public String getEndCustomerId() {
		return endCustomerId;
	}

	public void setEndCustomerId(String endCustomerId) {
		this.endCustomerId = endCustomerId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(String salesRepId) {
		this.salesRepId = salesRepId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public BigDecimal getAsp1() {
		return asp1;
	}

	public void setAsp1(BigDecimal asp1) {
		this.asp1 = asp1;
	}

	public BigDecimal getAsp2() {
		return asp2;
	}

	public void setAsp2(BigDecimal asp2) {
		this.asp2 = asp2;
	}

	public BigDecimal getAsp3() {
		return asp3;
	}

	public void setAsp3(BigDecimal asp3) {
		this.asp3 = asp3;
	}

	public BigDecimal getAsp4() {
		return asp4;
	}

	public void setAsp4(BigDecimal asp4) {
		this.asp4 = asp4;
	}

	public BigDecimal getAsp5() {
		return asp5;
	}

	public void setAsp5(BigDecimal asp5) {
		this.asp5 = asp5;
	}

	public BigDecimal getAsp6() {
		return asp6;
	}

	public void setAsp6(BigDecimal asp6) {
		this.asp6 = asp6;
	}

	public BigDecimal getAsp7() {
		return asp7;
	}

	public void setAsp7(BigDecimal asp7) {
		this.asp7 = asp7;
	}

	public BigDecimal getAsp8() {
		return asp8;
	}

	public void setAsp8(BigDecimal asp8) {
		this.asp8 = asp8;
	}

	public BigDecimal getAsp9() {
		return asp9;
	}

	public void setAsp9(BigDecimal asp9) {
		this.asp9 = asp9;
	}

	public BigDecimal getAsp10() {
		return asp10;
	}

	public void setAsp10(BigDecimal asp10) {
		this.asp10 = asp10;
	}

	public BigDecimal getAsp11() {
		return asp11;
	}

	public void setAsp11(BigDecimal asp11) {
		this.asp11 = asp11;
	}

	public BigDecimal getAsp12() {
		return asp12;
	}

	public void setAsp12(BigDecimal asp12) {
		this.asp12 = asp12;
	}

	public Integer getQuantity1() {
		return quantity1;
	}

	public void setQuantity1(Integer quantity1) {
		this.quantity1 = quantity1;
	}

	public Integer getQuantity2() {
		return quantity2;
	}

	public void setQuantity2(Integer quantity2) {
		this.quantity2 = quantity2;
	}

	public Integer getQuantity3() {
		return quantity3;
	}

	public void setQuantity3(Integer quantity3) {
		this.quantity3 = quantity3;
	}

	public Integer getQuantity4() {
		return quantity4;
	}

	public void setQuantity4(Integer quantity4) {
		this.quantity4 = quantity4;
	}

	public Integer getQuantity5() {
		return quantity5;
	}

	public void setQuantity5(Integer quantity5) {
		this.quantity5 = quantity5;
	}

	public Integer getQuantity6() {
		return quantity6;
	}

	public void setQuantity6(Integer quantity6) {
		this.quantity6 = quantity6;
	}

	public Integer getQuantity7() {
		return quantity7;
	}

	public void setQuantity7(Integer quantity7) {
		this.quantity7 = quantity7;
	}

	public Integer getQuantity8() {
		return quantity8;
	}

	public void setQuantity8(Integer quantity8) {
		this.quantity8 = quantity8;
	}

	public Integer getQuantity9() {
		return quantity9;
	}

	public void setQuantity9(Integer quantity9) {
		this.quantity9 = quantity9;
	}

	public Integer getQuantity10() {
		return quantity10;
	}

	public void setQuantity10(Integer quantity10) {
		this.quantity10 = quantity10;
	}

	public Integer getQuantity11() {
		return quantity11;
	}

	public void setQuantity11(Integer quantity11) {
		this.quantity11 = quantity11;
	}

	public Integer getQuantity12() {
		return quantity12;
	}

	public void setQuantity12(Integer quantity12) {
		this.quantity12 = quantity12;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
