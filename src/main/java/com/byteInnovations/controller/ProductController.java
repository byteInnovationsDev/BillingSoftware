package com.byteInnovations.controller;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.byteInnovations.model.Category;
import com.byteInnovations.model.Product;
import com.byteInnovations.model.Purchase;
import com.byteInnovations.model.User;
import com.byteInnovations.service.BillingService;
import com.byteInnovations.service.ProductService;
import com.byteInnovations.service.PurchaseService;
import com.byteInnovations.service.UserSessionService;
import com.byteInnovations.service.productServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {


	@Autowired
	private ProductService pser;
	@Autowired
	private BillingService ser;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PurchaseService purchaseSer;
	
	@Autowired
    private UserSessionService userSessionService;
	
	@GetMapping("/product")
	public String home(Model model, HttpSession session,  HttpServletResponse response){
		
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
		
		String billingType = "DINE";
		model.addAttribute("products", pser.findAllByOrderByIdAsc(billingType));
		model.addAttribute("categories", ser.findAll());
		return "product";
	}
	
	@GetMapping("/getAll")
	@ResponseBody
	public Map<String, Object> getAll(HttpServletRequest request){
		
		String billingType = request.getParameter("billingType");
		List<Product> product =  pser.findAllByOrderByIdAsc(billingType);
	    Map<String, Object> response = new HashMap<>();
	   
		for(Product p : product)
		{
			p.setCategory(ser.findNameById(p.getProdCategoryId()));
			
		}
		response.put("product", product);
		
		return response;
	}
	
	@GetMapping("/get/productById/{id}")
	@ResponseBody
	public Map<String, Object> fetch(@PathVariable int id) {
	    Map<String, Object> response = new HashMap<>();
	    Product product = pser.findById(id);
	    response.put("product", product);
	    String catName = ser.findNameById(product.getProdCategoryId());
	    Category cat = new Category();
	    cat.setId(product.getProdCategoryId());
	    cat.setName(catName);
	    response.put("category", cat); 

	    return response;
	}
	
	@PostMapping("/update/products")
	public ResponseEntity<?> updateProduct(@RequestBody Map<String, Object> data, HttpServletRequest request) {
		
		String billingType = request.getParameter("billingType");
	    Product product = objectMapper.convertValue(data.get("product"), Product.class);
	    Category category = objectMapper.convertValue(data.get("category"), Category.class);
	    pser.updateProduct(product,category, billingType);

	    return ResponseEntity.ok("done");
	}

	
	@PostMapping("/save/products")
	public ResponseEntity<?> saveProduct(@RequestBody Map<String, Object> data, HttpServletRequest request) {
		String billingType = request.getParameter("billingType");
	    Product product = objectMapper.convertValue(data.get("product"), Product.class);
	    Category category = objectMapper.convertValue(data.get("category"), Category.class);
	    pser.saveProduct(product,category, billingType);

	    return ResponseEntity.ok("in db");
	}
	
	@DeleteMapping("/delete/productById/{id}")
	//@ResponseBody
	public String delete(@PathVariable int id) {
		pser.deleteById(id);

		return "product";
	}
	
	@GetMapping("/get/productByName/{name}")
	@ResponseBody
	public Map<String, Object> search(@PathVariable String name, @RequestParam String billingType) {
	    Map<String, Object> response = new HashMap<>();
		
	    List<Product> product = pser.findByName(name, billingType);
	    
	    for(Product prod : product) {
	    	
	    	String catName = ser.findNameById(prod.getProdCategoryId());
	    	prod.setCategory(catName);
	    }
	    response.put("product", product);

	    return response;
	}
	
	

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
	    if (file.isEmpty()) {
	        return ResponseEntity
	                .badRequest()
	                .body("No file uploaded.");
	    }

	    try (InputStream is = file.getInputStream();
	         Workbook workbook = new XSSFWorkbook(is)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        StringBuilder result = new StringBuilder();

	        for (Row row : sheet) {
	            for (Cell cell : row) {
	                result.append(cell.toString()).append(" | ");
	            }
	            result.append("<br/>");
	        }
	        System.out.println("=========="+result);
	        return ResponseEntity
	                .ok()
	                .body(result.toString());

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error reading Excel file: " + e.getMessage());
	    }
	}

	
	
	@PostMapping("/upload/purchase")
	public ResponseEntity<?> uploadPurchaseFile(@RequestParam("file") MultipartFile file) {
	    Map<String, Object> response = new HashMap<>();

	    if (file.isEmpty()) {
	        response.put("success", false);
	        response.put("messages", List.of("No file uploaded."));
	        return ResponseEntity.ok(response);
	    }

	    try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        List<String> messages = new ArrayList<>();
	        List<Purchase> purchaseList = new ArrayList<>();
	        boolean isValid = true;

	        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null || isRowEmpty(row)) continue;

	            Purchase purchase = new Purchase();
	            boolean rowValid = true;

	            String product = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString().trim();
	            String quantity = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString().trim();
	            Cell costCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	            Cell dateCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

	            // Product
	            if (product.isEmpty()) {
	                messages.add("Row " + (i + 1) + ": Product is missing.");
	                rowValid = false;
	            } else {
	                purchase.setProduct(product);
	            }

	            // Quantity
	            if (quantity.isEmpty()) {
	                messages.add("Row " + (i + 1) + ": Quantity is missing.");
	                rowValid = false;
	            } else {
	                purchase.setQuantity(quantity);
	            }

	            // Cost
	            try {
	                double cost = costCell.getNumericCellValue();
	                if (cost <= 0) {
	                    throw new IllegalArgumentException("Cost must be positive.");
	                }
	                purchase.setCost(cost);
	            } catch (Exception e) {
	                messages.add("Row " + (i + 1) + ": Invalid cost.");
	                rowValid = false;
	            }

	            // Date
	            try {
	            	 LocalDate localDate = parseDateCell(dateCell);
	                 System.out.println("Parsed Date (yyyy-MM-dd): " + localDate); // Debug print
	                 purchase.setPurchaseDate(localDate);
	            } catch (Exception e) {
	                messages.add("Row " + (i + 1) + ": Invalid or missing purchase date.");
	                rowValid = false;
	            }

	            if (rowValid) {
	                purchaseList.add(purchase);
	            } else {
	                isValid = false;
	            }
	        }

	        if (isValid) {
	            for (Purchase purchase : purchaseList) {
	                purchaseSer.insertPurchase(
	                        purchase.getProduct(),
	                        purchase.getQuantity(),
	                        purchase.getCost(),
	                        purchase.getPurchaseDate()
	                );
	            }
	            response.put("success", true);
	            response.put("processedRows", purchaseList.size());
	        } else {
	            response.put("success", false);
	            response.put("processedRows", purchaseList.size());
	            response.put("messages", messages);
	        }

	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("messages", List.of("Error processing Excel file: " + e.getMessage()));
	        return ResponseEntity.ok(response);
	    }
	}

	private LocalDate parseDateCell(Cell dateCell) throws Exception {
	    if (dateCell == null) throw new Exception("Cell is null");

	    if (dateCell.getCellType() == CellType.STRING) {
	        String dateStr = dateCell.getStringCellValue().trim();
	        System.out.println("🔍 Raw string date: " + dateStr); // Debug

	        // Try only dd/MM/yyyy since that's what you're using
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        return LocalDate.parse(dateStr, formatter);
	    }

	    if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
	        // fallback in case someone still sends Excel date
	        LocalDate parsedDate = dateCell.getDateCellValue()
	                .toInstant()
	                .atZone(ZoneId.systemDefault())
	                .toLocalDate();
	        System.out.println("🔍 Fallback Parsed Date (yyyy-MM-dd): " + parsedDate);
	        return parsedDate;
	    }

	    throw new Exception("Unsupported or invalid date format: " + dateCell.toString());
	}


	@PostMapping("/check/category")
	public ResponseEntity<?> checkCategory(@RequestBody Product product) {

		boolean check = pser.checkCategoryProduct(product);
		System.out.println("================================================="+check);
	    return ResponseEntity.ok(check);
	}
	
	
	private boolean isRowEmpty(Row row) {
	    if (row == null) return true;
	    for (int c = 0; c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	        if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
	            return false;
	        }
	    }
	    return true;
	}
}

/*
@PostMapping("/upload/purchase")
	public ResponseEntity<?> uploadPurchaseFile(@RequestParam("file") MultipartFile file) {
	    Map<String, Object> response = new HashMap<>();

	    if (file.isEmpty()) {
	        response.put("success", false);
	        response.put("messages", List.of("No file uploaded."));
	        return ResponseEntity.ok(response);
	    }
	    
	    try (InputStream is = file.getInputStream();
	         Workbook workbook = new XSSFWorkbook(is)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        SimpleDateFormat inputFormat = new SimpleDateFormat("d/M/yyyy");
	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

	        List<String> messages = new ArrayList<>();
	        List<Purchase> purchaseList = new ArrayList<>();
	        boolean isValid = true;
	        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Purchase purchase = new Purchase();
	           
	            String product = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString().trim();
	            String quantity = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString().trim();
	            Cell costCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	            Cell dateCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

	           

	            if (product.isEmpty()) {
	                messages.add("Row " + i + ": Product is missing.");
	                isValid = false;
	            } else {
	                purchase.setProduct(product);
	            }

	            if (quantity.isEmpty()) {
	                messages.add("Row " + i+ ": Quantity is missing.");
	                isValid = false;
	            } else {
	                purchase.setQuantity(quantity);
	            }

	            try {
	                double cost = costCell.getNumericCellValue();
	                if (cost < 0 || cost == 0.0) {
	                    messages.add("Row " + i  + ": Cost cannot be negative.");
	                    isValid = false;
	                } else {
	                    purchase.setCost(cost);
	                }
	            } catch (Exception e) {
	                messages.add("Row " + i  + ": Cost is invalid.");
	                isValid = false;
	            }

	            try {
	            	Cell datecell = row.getCell(3);

	            	if (datecell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(datecell)) {
	            	    // Excel date cell (properly formatted as date)
	            	    Date date = datecell.getDateCellValue();
	            	    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	            	    purchase.setPurchaseDate(localDate);

	            	} else if (datecell.getCellType() == CellType.STRING) {
	            	    // Date given as text (e.g., "15/06/2025")
	            	    String dateStr = datecell.getStringCellValue().trim();
	            	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	            	    LocalDate localDate = LocalDate.parse(dateStr, inputFormatter);
	            	    purchase.setPurchaseDate(localDate);

	            	} else {
	            	    throw new Exception("Unrecognized or unsupported date format in Excel cell");
	            	}

	                
	                purchaseList.add(purchase);
	            } catch (Exception e) {
	                messages.add("Row " + i  + ": Purchase date is invalid.");
	                isValid = false;
	            }

	            
	        }
	       
	        	if (isValid) {
	        		 for(Purchase purchase : purchaseList) {
	                purchaseSer.insertPurchase(
	                        purchase.getProduct(),
	                        purchase.getQuantity(),
	                        purchase.getCost(),
							purchase.getPurchaseDate());
					response.put("success", true);

				}
			} else {

				response.put("success", false);
				response.put("processedRows", purchaseList.size());
				response.put("messages", messages);
			} 
	       
	        
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("messages", List.of("Error reading Excel file: " + e.getMessage()));
	        return ResponseEntity.ok(response);
	    }
	   
	}

*/