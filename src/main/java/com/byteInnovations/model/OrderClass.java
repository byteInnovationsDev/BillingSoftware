package com.byteInnovations.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bi_ma_order")
public class OrderClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "sub_category")
    private String subCategory;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "payment_type", length = 50)
    private String paymentType;

    @Column(name = "order_type", length = 50)
    private String orderType;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "customer_phone_number", length = 20)
    private String customerPhoneNumber;

    @Column(name = "order_date")
    private LocalDate orderDate;

    // Getters and setters
    
    
    
    public Long getOrderId() {
        return orderId;
    }

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

	public OrderClass(Long orderId, String productName, Integer quantity, BigDecimal price, String paymentType,
			String orderType, String customerName, String customerPhoneNumber, LocalDate orderDate) {
		super();
		this.orderId = orderId;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
		this.paymentType = paymentType;
		this.orderType = orderType;
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.orderDate = orderDate;
	}

	public OrderClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "OrderClass [orderId=" + orderId + ", productName=" + productName + ", quantity=" + quantity + ", price="
				+ price + ", paymentType=" + paymentType + ", orderType=" + orderType + ", customerName=" + customerName
				+ ", customerPhoneNumber=" + customerPhoneNumber + ", orderDate=" + orderDate + "]";
	}
    
    
}
