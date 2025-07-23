package com.byteInnovations.service;

import java.time.LocalDate;
import java.util.List;

import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Purchase;
import com.byteInnovations.model.Report;

public interface ReportService {
	
	 List<Report> findByDesc(String desc);
	
	 List<OrderClass> findOrdersBetweenDates(LocalDate fromdate , LocalDate toDate);
	 
	 List<OrderClass> findOrdersBetweenDatesGroupByProduct(LocalDate fromdate , LocalDate toDate);
	 
	List<Purchase> findPurcheasesBetweenDates(LocalDate fromdate, LocalDate todate);
	 
}
