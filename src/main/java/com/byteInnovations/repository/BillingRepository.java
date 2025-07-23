package com.byteInnovations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.byteInnovations.model.Category;

@Repository
public interface BillingRepository extends JpaRepository<Category, Integer> {

	public List<Category> findAll();

	@Query("SELECT c FROM Category c WHERE c.name = :categoryName ")
	Category findByName(@Param("categoryName") String categoryName);
	
	

	// @Query("SELECT COALESCE(c.id, 0) FROM Category c WHERE c.name = :product")
	@Query("SELECT COALESCE((SELECT c.id FROM Category c WHERE c.name = :product), 0)")
	int findIdByProdName(@Param("product") String product);

	@Query("SELECT c.name FROM Category c WHERE c.id = :id ")
	String findNameById(int id);

	
	  @Query("SELECT COUNT(c.name) FROM Category c WHERE c.name = :categoryName ")
	  int findCountByName(@Param("categoryName") String categoryName);
	 

	/*
	 * @Modifying
	 * 
	 * @Query("UPDATE Category c SET c.name = :name, c.parentFlag = :parentFlag WHERE c.id = :id"
	 * ) void updateCategory(
	 * 
	 * @Param("id") int id,
	 * 
	 * @Param("name") String name,
	 * 
	 * @Param("parentFlag") String parentFlag
	 * 
	 * );
	 */
	/* public void updateCategory(int id, String name, String parentFlag); */

}
