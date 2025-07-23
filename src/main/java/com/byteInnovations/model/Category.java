package com.byteInnovations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bi_ma_category")
public class Category {

	@Id
	@Column(name = "cat_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "cat_name")
	private String name;
	@Column(name = "parent_flag")
	private String parentFlag;
	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category() {
		super();
	}

	public Category(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
	
	public String getParentFlag() {
		return parentFlag;
	}


	public void setParentFlag(String parentFlag) {
		this.parentFlag = parentFlag;
	}


	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", parentFlag=" + parentFlag + "]";
	}


	

}
