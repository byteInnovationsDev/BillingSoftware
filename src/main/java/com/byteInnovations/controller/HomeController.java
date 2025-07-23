// HomeController.java
package com.byteInnovations.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.byteInnovations.service.BillingService;
import com.byteInnovations.service.ProductService;


import com.byteInnovations.model.Category;
import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Product;
import com.byteInnovations.repository.OrderRepository;

@Controller
public class HomeController {

    private final ProductController productController;

    @Autowired
    private BillingService ser;
    
    @Autowired
    private ProductService pser;
    @Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
    private OrderRepository orderRepo;

    HomeController(ProductController productController) {
        this.productController = productController;
    }
    
    @GetMapping("/")
    public String home(Model model) {
        
        return "index";
    }
    
    @GetMapping("/billing")
    public String billing(Model model) {
        List<Category> category = ser.findAll();
        System.out.println(category);
        model.addAttribute("Categories", category);
        return "billing";
    }
    
    @GetMapping("/get/product/{category}")
    @ResponseBody
    public List<Product> ProductsByCategory(@PathVariable String category)
    {
		List<Product> prodList= ser.findIdByName(category);
    	System.out.println(prodList);
    	return prodList;
    }
    
    @GetMapping("/get/Categories")
    @ResponseBody
    public List<Category> getCatrgories(Model model) {
        List<Category> category = ser.findAll();
        System.out.println(category);
        return category;
    }
    
    @GetMapping("/get/productsByName/{product}")
    @ResponseBody
    public List<Product> getProducts(@PathVariable String product)
    {
    	List<Product> productList = pser.getProducts(product); 
    	System.out.println("===================="+productList);
		return productList;
    }
    
    @GetMapping("/Print")
    public String print(Model model) {
       
        return "Print";
    }
    
    @PostMapping("/save/order")
    public ResponseEntity<String> submitOrder(@RequestBody Map<String, Object> data) {
        // 1. Convert customerInfo into a Map
        Map<String, String> customerInfo = objectMapper.convertValue(
            data.get("customerInfo"),
            new TypeReference<>() {}
        );

        // 2. Convert products into List of Map
        List<Map<String, String>> productList = objectMapper.convertValue(
            data.get("products"),
            new TypeReference<>() {}
        );

        List<OrderClass> ordersToSave = new ArrayList<>();

        for (Map<String, String> prod : productList) {
            OrderClass order = new OrderClass();

            order.setProductName(prod.get("product"));
            order.setQuantity(Integer.parseInt(prod.getOrDefault("quantity", "0")));
            order.setPrice(new BigDecimal(prod.getOrDefault("price", "0.00")));
            order.setCategory(prod.get("category"));
            order.setSubCategory(prod.get("subCategory"));
            //System.out.println("0000000000000000000000    "+prod.get("subCategory"));
            // Set customer info
            order.setCustomerName(customerInfo.get("customerName"));
            order.setCustomerPhoneNumber(customerInfo.get("customerPhNum"));
            order.setPaymentType(customerInfo.get("paymentType"));
            order.setOrderType(customerInfo.get("orderType"));
            order.setOrderDate(LocalDate.now());

            ordersToSave.add(order);
        }
       // System.out.println("=================================="+ordersToSave);
        orderRepo.saveAll(ordersToSave);
        return ResponseEntity.ok("Order submitted successfully");
    }


}
