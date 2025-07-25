package com.byteInnovations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.byteInnovations.model.Product;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	//List<Product> getByProdParentName(String category);
	@Query("SELECT p FROM Product p  WHERE p.prodCategoryId = :id")
	List<Product> findDistinctProdParentNamesById(@Param("id")int id);
	
	//int findProdCategoryIdBy(String category);
	
	@Query("SELECT p FROM Product p WHERE p.prodCategoryId = :id")
    List<Product> findNameById(@Param("id") int id);
	
	@Query("SELECT DISTINCT p FROM Product p  WHERE p.prodParentName = :prodParentName AND prodParentFlag = 'Y'")
	List<Product> findNameByProdParentName(@Param("prodParentName")String prodParentName);
	
	List<Product> findAll();

	@Query("SELECT p FROM Product p ORDER BY p.id ASC")
	List<Product> findAllByOrderByIdAsc();

	@Query("SELECT p FROM Product p WHERE p.id = :id")
	Product findById(@Param("id") int id);
	
	@Query("SELECT p FROM Product p  WHERE p.prodParentName = :prodParentName AND p.prod_name = :prod_name ")
	Product findByNameByProdParentName(@Param("prod_name")String prod_name,@Param("prodParentName") String prodParentName);
	
	@Modifying
	@Query("UPDATE Product p SET p.prod_name = :prodName, p.prodParentName = :prodParentName, p.price = :price, p.prodDispFlag = :prodDispFlag,  p.prodCategoryId = :prodCategoryId WHERE p.id = :id")
	void updateProduct(
	    @Param("id") int id,
	    @Param("prodName") String prodName,
	    @Param("prodParentName") String prodParentName,
	    @Param("price") Double price,
	    @Param("prodDispFlag") String prodDispFlag,
	    @Param("prodCategoryId") int prodCategoryId
	);
	
	 @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.id = :id")
    void deleteAllById(@Param("id") int id);

	 @Query("SELECT p FROM Product p WHERE p.prod_name LIKE %:prod_name%")
	 List<Product> findByName(@Param("prod_name") String prod_name);

	 @Query("SELECT COUNT(p.prod_name) FROM Product p  WHERE p.prodParentName = :prodParentName AND p.prod_name = :prod_name ")
	 int findCountByNameByProdParentName(@Param("prod_name")String prod_name,@Param("prodParentName") String prodParentName);
		
	
}
