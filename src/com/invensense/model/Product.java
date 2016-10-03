package com.invensense.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Product")
@XmlAccessorType(XmlAccessType.NONE)
@Entity(name="PRODUCT")
public class Product extends BaseModel implements Comparable<Product> {

	@Id
	@XmlElement(name="Id")
	private String id ;

	@XmlElement(name="Name")
	private String productName ;
	
	@XmlElement(name="Status")
	private String status ;
	
	@XmlElement(name="Description")
	private String description ;
	
	@XmlElement(name="ProductCategory")
	private String productCategory ;
	
	@XmlElement(name="PartNumber")
	private String partNumber ;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	@Override
	public int compareTo(Product product) {
		return productName.compareTo(product.getProductName());
	}
}
