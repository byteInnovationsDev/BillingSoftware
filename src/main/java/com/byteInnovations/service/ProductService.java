package com.byteInnovations.service;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.byteInnovations.model.Category;
import com.byteInnovations.model.Product;

public interface ProductService {
	
	
		List<Product> getProdParentNameById(int id);
		List<Product> getProducts(String product, String billingType);
		//List<String> findNameById(int id);
		List<Product> findAll();
		List<Product> findAllByOrderByIdAsc(String billingType);
		
		Product findById(int id);
		
		void updateProduct(Product p, Category c, String billingType);
		void saveProduct(Product product, Category category, String billingType);
		void deleteById(int id);
		List<Product> findByName(String prod_name, String billingType);
		
		//Product findByNameByProdParentName(String prod_name, String prodParentName);
		
		boolean checkCategoryProduct(Product product);

}
