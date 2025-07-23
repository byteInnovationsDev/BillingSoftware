// BillingService.java
package com.byteInnovations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.byteInnovations.model.Category;
import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Product;

public interface BillingService {
    List<Category> findAll();

	//int getIdByName(String cat_name);

	List<Product> findIdByName(String category);
	
	int findIdByProdName(String product );
	
	String findNameById(int id);
	
	int findCountByName(String categoryName);

	void save(Category category);

	Category findByName(String categoryName);
	
	//void updateCategory(int id, String name, String parentFlag);
	
	OrderClass saveOrder(OrderClass order);
    OrderClass getOrderById(int id);
}
