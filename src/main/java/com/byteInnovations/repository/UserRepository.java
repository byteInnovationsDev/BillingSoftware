package com.byteInnovations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.byteInnovations.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	  
	  @Query("SELECT u FROM User u where u.userType !='A'")
	  List<User> getUsersList();
	 
	  Optional<User> findByUserId(int userId);
}


