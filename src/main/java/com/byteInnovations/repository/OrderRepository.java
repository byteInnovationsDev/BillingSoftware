package com.byteInnovations.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.byteInnovations.model.OrderClass;
import com.byteInnovations.model.Report;

public interface OrderRepository  extends JpaRepository<OrderClass, Integer>{
		
	
	@Query("SELECT o FROM OrderClass o WHERE o.orderDate BETWEEN :fromDate AND :toDate ORDER BY o.id")
    List<OrderClass> findOrdersBetweenDates(@Param("fromDate") LocalDate fromDate,
                                       @Param("toDate") LocalDate toDate);
		
	/*
	 * @Query("SELECT o.productName,SUM(PRICE) FROM OrderClass o WHERE o.orderDate BETWEEN :fromDate AND :toDate GROUP BY o.productName"
	 * ) List<OrderClass> findOrdersBetweenDatesGroupByProduct(@Param("fromDate")
	 * LocalDate fromDate,
	 * 
	 * @Param("toDate") LocalDate toDate);
	 */
    @Query("SELECT o.productName,o.category, o.subCategory ,COUNT(o.productName), SUM(o.price) FROM OrderClass o WHERE o.orderDate BETWEEN :fromDate AND :toDate GROUP BY o.productName,o.category, o.subCategory")                                  
   	List<Object[]> findOrdersBetweenDatesGroupByProduct(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
   
   	
	
}
