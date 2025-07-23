package com.byteInnovations.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.byteInnovations.model.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
	
	@Modifying
	@Query(value = "INSERT INTO bi_ma_purchase (product, quantity, cost, purchase_date) VALUES (:product, :quantity, :cost, :purchaseDate)", nativeQuery = true)
	void insertPurchase(@Param("product") String product,
	                    @Param("quantity") String quantity,
	                    @Param("cost") double cost,
	                    @Param("purchaseDate") LocalDate purchaseDate);
	
	
	  @Query("SELECT p.product, p.quantity ,p.cost, p.purchaseDate FROM Purchase p WHERE p.purchaseDate BETWEEN :fromDate AND :toDate")                                  
	   	List<Object[]> findPurchasesBetweenDates(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);


	   
	
}