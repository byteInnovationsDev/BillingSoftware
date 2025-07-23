package com.byteInnovations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bi_ma_reportheader")
public class Report {
	
	
	@Id
	@Column(name = "rh_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "rh_name")
	private String name;
	
	@Column(name = "rh_desc")
	private String description;
	
	@Column(name = "rh_dis_order")
	private int disOrder;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public int getDisOrder() {
		return disOrder;
	}

	public void setDisOrder(int disOrder) {
		this.disOrder = disOrder;
	}

	public Report(int id, String name, String Description, int disOrder) {
		super();
		this.id = id;
		this.name = name;
		this.description = Description;
		this.disOrder = disOrder;
	}

	public Report() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Report [id=" + id + ", name=" + name + ", desc=" + description + ", disOrder=" + disOrder + "]";
	}
	
	
	
}
