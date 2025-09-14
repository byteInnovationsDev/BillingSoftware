package com.byteInnovations.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byteInnovations.model.User;
import com.byteInnovations.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public List<User> getUsersList() {
		
		return userRepo.getUsersList();
	}

	@Override
	public void saveUser(User user) {
		userRepo.save(user);
	}

	@Override
	public Optional<User> getUserById(int userId) {
		Optional<User> user = userRepo.findById(userId);
		return user;
	}

	@Override
	public void deleteUserById(int userId) {
		this.userRepo.deleteById(userId);
	}
	@Override
	public User authenticate(int userId, String password) {
	    Optional<User> optionalUser = userRepo.findByUserId(userId);  // or findById if numeric

	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();

	        // For simple (not secure) password check:
	        if (user.getUserPassword().equals(password)) {
	            return user;
	        }

	        // If you use hashed passwords, use BCryptPasswordEncoder.matches(raw, hashed)
	    }

	    return null;  // authentication failed
	}

}
