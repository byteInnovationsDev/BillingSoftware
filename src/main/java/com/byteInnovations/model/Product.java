package com.byteInnovations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bi_ma_product")
public class Product {
	
	@Id
	@Column(name = "prod_id")
	private int id;
	
	@Column(name ="prod_name")
	private String prod_name;
	
	private double price;
	
	@Column(name = "prod_cat_id")
	private int prodCategoryId;

	@Column(name = "prod_disp_flag")
	private String prodDispFlag;
	
	@Column(name = "prod_parent_name")
	private String prodParentName;
	
	@Column(name = "prod_parent_flag")
	private String prodParentFlag;
	
	
	private String category;
	private String subcategory;
	
	
	public String getSubcategory() {
		return subcategory;
	}
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getProdCategoryId() {
		return prodCategoryId;
	}
	public void setProdCategoryId(int prodCategoryId) {
		this.prodCategoryId = prodCategoryId;
	}
	public String getProdDispFlag() {
		return prodDispFlag;
	}
	public void setProdDispFlag(String prod_disp_flag) {
		this.prodDispFlag = prod_disp_flag;
	}
	public String getProdParentName() {
		return prodParentName;
	}
	public void setProdParentName(String prodParentName) {
		this.prodParentName = prodParentName;
	}
	public String getProdParentFlag() {
		return prodParentFlag;
	}
	public void setProdParentFlag(String prodParentFlag) {
		this.prodParentFlag = prodParentFlag;
	}
	
	
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", prod_name=" + prod_name + ", price=" + price + ", prodCategoryId="
				+ prodCategoryId + ", prodDispFlag=" + prodDispFlag + ", prodParentName=" + prodParentName
				+ ", prodParentFlag=" + prodParentFlag + ", category=" + category + ", subcategory=" + subcategory
				+ "]";
	}
	public Product(int id, String prod_name, double price, int prodCategoryId, String prodDispFlag,
			String prodParentName, String prodParentFlag, String category, String subcategory) {
		super();
		this.id = id;
		this.prod_name = prod_name;
		this.price = price;
		this.prodCategoryId = prodCategoryId;
		this.prodDispFlag = prodDispFlag;
		this.prodParentName = prodParentName;
		this.prodParentFlag = prodParentFlag;
		this.category = category;
		this.subcategory = subcategory;
	}
	
	
	
	
	
	
}
