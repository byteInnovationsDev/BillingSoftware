// HomeController.java
package com.byteInnovations.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.byteInnovations.service.BillingService;
import com.byteInnovations.service.ProductService;
import com.byteInnovations.service.UserService;
import com.byteInnovations.service.UserSessionService;
import com.byteInnovations.model.Category;
import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Product;
import com.byteInnovations.model.User;
import com.byteInnovations.model.UserSession;
import com.byteInnovations.repository.OrderRepository;

@Controller
public class HomeController {

    private final ProductController productController;

    @Autowired
    private BillingService ser;
    
    @Autowired
    private ProductService pser;
    
    @Autowired
    private UserService userRepo;
    
    @Autowired
    private UserSessionService userSessionService;
    
    @Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
    private OrderRepository orderRepo;

    HomeController(ProductController productController) {
        this.productController = productController;
    }
    
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
        	Long userSessionId = (Long) session.getAttribute("userSessionId");
        	  userSessionService.recordLogout(userSessionId);
            session.invalidate();
        }

        return "login";
    }
    @PostMapping("/login")
    public String login(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            // Set user details to model
            model.addAttribute("userName", user.getUserName());
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("userType", user.getUserType());

            // Record login time
            UserSession userSession = userSessionService.recordLogin((long) user.getUserId());
            session.setAttribute("userSessionId", userSession.getId());

            // Check user type and route accordingly
            if ("U".equals(user.getUserType())) {
                List<Category> category = ser.findAll();
                model.addAttribute("Categories", category);
                return "billing"; // Go to billing page
            }

            return "index"; // Default landing page
        }

        // If no user in session, redirect to login page
        return "redirect:/login-page";
    }

    
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        Long userSessionId = (Long) session.getAttribute("userSessionId");
        if (userSessionId != null) {
            try {
                userSessionService.recordLogout(userSessionId);
            } catch (RuntimeException e) {
                System.out.println("Could not record logout: " + e.getMessage());
            }
        }
        session.invalidate();

        response.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\", \"executionContexts\"");

        return "redirect:/login-page";  // Your login page URL
    }

    
    @GetMapping("/index")
    public String index(Model model, HttpSession session,  HttpServletResponse response) {
    	User user = (User) session.getAttribute("user");
    	
    	if (user != null  && user.getUserType().equals("A")) {
    		model.addAttribute("userName", user.getUserName());
    		model.addAttribute("userId", user.getUserId());
    		model.addAttribute("userType", user.getUserType());
    	} else {
    	    Long userSessionId = (Long) session.getAttribute("userSessionId");
    		userSessionService.recordLogout(userSessionId);
    		session.invalidate();
    	    response.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\", \"executionContexts\"");
    		return "redirect:/login-page"; 
    	}
    	return "index"; 
    }
    @GetMapping("/login-page")
    public String showLoginForm(HttpSession session, HttpServletResponse response) {
        // Try to get session ID for logout logging
        Long userSessionId = (Long) session.getAttribute("userSessionId");

        if (userSessionId != null) {
            try {
                userSessionService.recordLogout(userSessionId);  // Logs logout time
            } catch (RuntimeException e) {
                System.out.println("Could not record logout: " + e.getMessage());
            }
        }

        session.invalidate();

        response.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\", \"executionContexts\"");

        return "login";
    }

    @GetMapping("/billing")
    public String billing(Model model, HttpSession session) {
    	 User user = (User) session.getAttribute("user");

         if (user != null) {
             model.addAttribute("userName", user.getUserName());
             model.addAttribute("userId", user.getUserId());
             model.addAttribute("userType", user.getUserType());
         } else {
             return "redirect:/login"; 
         }
        List<Category> category = ser.findAll();
        System.out.println(category);
        model.addAttribute("Categories", category);
        return "billing";
    }
    
    @GetMapping("/get/product/{category}")
    @ResponseBody
    public List<Product> ProductsByCategory(@PathVariable String category,  @RequestParam String billingType, HttpServletRequest request)
    {
		List<Product> prodList= ser.findIdByName(category, billingType);	
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
    public List<Product> getProducts(@PathVariable String product, @RequestParam String billingType)
    {
    	List<Product> productList = pser.getProducts(product,billingType ); 
		return productList;
    }
    
    @GetMapping("/Print")
    public String print(Model model) {
       
        return "Print";
    }
    
    @PostMapping("/save/order")
    public ResponseEntity<String> submitOrder(@RequestBody Map<String, Object> data) {

    	Map<String, String> customerInfo = objectMapper.convertValue(
            data.get("customerInfo"),
            new TypeReference<>() {}
        );

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
            order.setCustomerName(customerInfo.get("customerName"));
            order.setCustomerPhoneNumber(customerInfo.get("customerPhNum"));
            order.setPaymentType(customerInfo.get("paymentType"));
            order.setOrderType(customerInfo.get("orderType"));
            order.setOrderDate(LocalDate.now());

            ordersToSave.add(order);
        }
        orderRepo.saveAll(ordersToSave);
        return ResponseEntity.ok("Order submitted successfully");
    }
    
    @GetMapping("/error")
    public String handleError() {
        return "Error"; 
    }


}
