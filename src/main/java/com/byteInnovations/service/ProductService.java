package com.byteInnovations.service;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.byteInnovations.model.Category;
import com.byteInnovations.model.Product;

public interface ProductService {
	
	
		List<Product> getProdParentNameById(int id);
		List<Product> getProducts(String product);
		//List<String> findNameById(int id);
		List<Product> findAll();
		List<Product> findAllByOrderByIdAsc();
		
		Product findById(int id);
		
		void updateProduct(Product p, Category c);
		void saveProduct(Product product, Category category);
		void deleteById(int id);
		List<Product> findByName(String prod_name);
		
		//Product findByNameByProdParentName(String prod_name, String prodParentName);
		
		boolean checkCategoryProduct(Product product);

}
