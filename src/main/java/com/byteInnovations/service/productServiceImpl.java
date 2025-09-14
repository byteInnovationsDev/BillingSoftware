package com.byteInnovations.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.byteInnovations.controller.HomeController;
import com.byteInnovations.model.Category;
import com.byteInnovations.model.Product;
import com.byteInnovations.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service

public class productServiceImpl implements ProductService{

	@Autowired
	private ProductRepository repo;
	@Autowired
	private BillingService ser;


  
	@Override
	public List<Product> getProdParentNameById(int id) {

		List<Product> prodParentNames =  repo.findDistinctProdParentNamesById(id);
		return prodParentNames;
	}

	@Override
	public List<Product> getProducts(String product, String billingType) {
		
		List<Product> productList = new ArrayList<>();
		int id = ser.findIdByProdName(product);
		
		 if(id!= 0 ){
			 
		 productList = repo.findNameByIdType(id, billingType.toUpperCase());
			 
		 }else {
			 productList = repo.findNameByProdParentName(product, billingType.toUpperCase());
		 }
		
		return productList;
	}


	@Override
	public List<Product> findAll() {
		return repo.findAll();
	}


	@Override
	public List<Product> findAllByOrderByIdAsc(String billingType) {
		List<Product> list = repo.findAllByOrderByIdAsc(billingType);
		return list;
	}

	@Override
	public Product findById(int id) {
		
		Product product = repo.findById(id);
		
		return product;
	}

		@Override
	 	@Transactional
	    public void updateProduct(Product p, Category c, String billingType) {

		Product product = new Product();
		Product prod = new Product();
		product.setProd_name(p.getProd_name());
		product.setPrice(p.getPrice());
		product.setProdDispFlag(p.getProdDispFlag());
		product.setId(p.getId());
		String prodName = p.getProd_name();
		
		if(p.getProdParentName()!=null)
		{
			String prodParentname = p.getProdParentName();
			product.setProdParentFlag("Y");
			product.setProdParentName(prodParentname);
			/*
			 * System.out.println(prodName+"==============="+prodParentname); prod =
			 * repo.findByNameByProdParentName(c.getId(),prodParentnam);
			 */
			//System.out.println("+++++++++++++++++++++++++++"+product);
			//ser.updateCategory(c.getId(), c.getName(), product.getProdParentFlag());
			product.setProdCategoryId(c.getId());
				
		}else {
			
			String prodParentName = "NA";
			product.setProdParentFlag("N");
			product.setProdParentName(prodParentName);
		}
		
		
		repo.updateProduct(
				
				product.getId(),
		        product.getProd_name(),
		        product.getProdParentName(),
		        product.getPrice(),
		        product.getProdDispFlag(),
		        product.getProdCategoryId(),
		        billingType
		    );
	}

		@Override
		public void saveProduct(Product product, Category category, String billingType) {
			
			String categoryName = category.getName();
			
			int count = ser.findCountByName(categoryName);
			if(count == 0)
			{
				if(product.getProdParentName()!="" && product.getProdParentName()!=null && product.getProdParentName()!="NA") {
					
					category.setParentFlag("Y");
					
				}else {
					category.setParentFlag("N");
					
				}
				ser.save(category);
			}
			if (product.getProdParentName() != null && !product.getProdParentName().equals("") && !product.getProdParentName().equalsIgnoreCase("NA")) {
			    product.setProdParentFlag("Y");
			} else {
			    product.setProdParentFlag("N");
			}
			Category c = ser.findByName(categoryName);
			product.setProdCategoryId(c.getId());
			Integer maxId = repo.getMaxProductId();
		    product.setId(maxId + 1);
		    product.setBillType(billingType);
			repo.save(product);
			
		}

		@Override
		public void deleteById(int id) {
			repo.deleteAllById(id);
		}

		@Override
		public List<Product> findByName(String prod_name, String billType) {

			return repo.findByName(prod_name, billType);
		}

		@Override
		public boolean checkCategoryProduct(Product product) {
			System.out.println("++++++++++++++++++++++"+product);
			String prodName = product.getProd_name();
			String prodParentName = product.getSubcategory();
			int prodCount = repo.findCountByNameByProdParentName(prodName,prodParentName);
			int catCount = ser.findCountByName(product.getCategory());
			System.out.println("=================="+prodCount);
			System.out.println("=================="+catCount);
			if(prodCount == 0 || catCount == 0)
			{
				return true;
			}

			return false;
		}	
}
