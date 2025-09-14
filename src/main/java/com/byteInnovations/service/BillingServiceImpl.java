// BillingServiceImpl.java
package com.byteInnovations.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byteInnovations.model.Category;
import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Product;
import com.byteInnovations.repository.BillingRepository;
import com.byteInnovations.repository.OrderRepository;
import com.byteInnovations.repository.ProductRepository;

@Service
public class BillingServiceImpl implements BillingService {
	
    @Autowired
    private BillingRepository repo;
    @Autowired
    private ProductRepository prepo;
    @Override
    public List<Category> findAll() {
        return repo.findAll();
    }
    @Autowired
    private OrderRepository orderRepository;

	@Override
	public List<Product> findIdByName(String category, String billType) {
		
		Category cat = repo.findByName(category);
		List<Product> prodList = new ArrayList<>();
		if(cat.getParentFlag()!=null  && cat.getParentFlag().equalsIgnoreCase("Y"))
		{
			
			List<Product> fullList=  prepo.findDistinctProdParentNamesById(cat.getId());
			Set<String> subcat = new HashSet<String>(); 
			
			for(Product prod : fullList) {
				if(prod.getProdCategoryId() == cat.getId())
				{
					subcat.add(prod.getProdParentName());
				}else {
					continue;
				}
			}
			System.out.println(subcat);
			for(String s : subcat)
			{
				Product p = new Product();
				p.setProdParentName(s);
				p.setProdParentFlag("Y");
				
				prodList.add(p);
			}
			
		}else {
			
			prodList =  prepo.findNameByIdType(cat.getId(), billType.toUpperCase());
		}
		
		return prodList;
	}

	@Override
	public int findIdByProdName(String product) {
		
		return repo.findIdByProdName(product);
	}

	@Override
	public String findNameById(int id) {
		
		return repo.findNameById(id);
	}

	@Override
	public int findCountByName(String categoryName) {
		return repo.findCountByName(categoryName);
	}

	@Override
	public void save(Category category) {
		
		repo.save(category);
	}

	@Override
	public Category findByName(String categoryName) {
		return repo.findByName(categoryName);
	}

	  @Override
	    public OrderClass saveOrder(OrderClass order) {
	        return orderRepository.save(order);
	    }
	    
	    @Override
	    public OrderClass getOrderById(int id) {
	        return orderRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Order not found"));
	    }
}
