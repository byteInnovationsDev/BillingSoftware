package com.byteInnovations.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byteInnovations.model.Purchase;
import com.byteInnovations.repository.PurchaseRepository;
@Service
public class PurchaseServiceImpl implements PurchaseService{

	
	 @Autowired
	    private PurchaseRepository purchaseRepo;

	    public void insertPurchase(String product, String quantity, double cost, LocalDate purchaseDate) {
	        Purchase purchase = new Purchase();
	        purchase.setProduct(product);
	        purchase.setQuantity(quantity);
	        purchase.setCost(cost);
	        purchase.setPurchaseDate(purchaseDate); 

	        purchaseRepo.save(purchase);
	    }

		@Override
		public List<Object[]> findPurcheasesBetweenDates(LocalDate fromDate, LocalDate toDate) {
			
			return purchaseRepo.findPurchasesBetweenDates(fromDate, toDate);	
		}

}
