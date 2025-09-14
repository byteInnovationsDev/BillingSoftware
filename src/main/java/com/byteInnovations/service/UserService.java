package com.byteInnovations.service;

import java.util.List;
import java.util.Optional;

import com.byteInnovations.model.User;

public interface UserService {
	
	List<User> getUsersList();

	void saveUser(User user);
	
	Optional<User> getUserById(int userId);

	void deleteUserById(int userId);

	public User authenticate(int userId, String password);

}
