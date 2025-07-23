package com.byteInnovations.service;

import java.time.LocalDate;
import java.util.List;


public interface PurchaseService {
	
	void insertPurchase(String name, String quantity, double cost , LocalDate purchaseDate);
	
	List<Object[]> findPurcheasesBetweenDates(LocalDate fromDate,  LocalDate toDate);
}
