package com.byteInnovations.model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bi_ma_purchase")
public class Purchase {
	
	@Id
	@Column(name = "prod_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String product;
	private String quantity;
	private double cost;
	@Column(name = "purchase_date")
	private LocalDate purchaseDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Purchase(int id, String product, String quantity, double cost, LocalDate purchaseDate) {
		super();
		this.id = id;
		this.product = product;
		this.quantity = quantity;
		this.cost = cost;
		this.purchaseDate = purchaseDate;
	}
	public Purchase() {
		super();
	}
	
	@Override
	public String toString() {
		return "purchase [id=" + id + ", product=" + product + ", quantity=" + quantity + ", cost=" + cost
				+ ", purchaseDate=" + purchaseDate + "]";
	}
	
	
	
}
