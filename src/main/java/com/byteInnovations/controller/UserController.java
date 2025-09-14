package com.byteInnovations.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.byteInnovations.model.User;
import com.byteInnovations.service.UserService;
import com.byteInnovations.service.UserSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ui.Model; 
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private UserService userRepo;
	
	@Autowired
    private UserSessionService userSessionService;
	
	@GetMapping("/user")
	public String home(Model model , HttpSession session,  HttpServletResponse response) {
		User user = (User) session.getAttribute("user");
    	
    	if (user != null  && user.getUserType().equals("A")) {
    		model.addAttribute("userName", user.getUserName());
    		model.addAttribute("userId", user.getUserId());
    		model.addAttribute("userType", user.getUserType());
    	} else {
    	    Long userSessionId = (Long) session.getAttribute("userSessionId");
    	    userSessionService.recordLogout(userSessionId);
    	    session.invalidate();
    	    response.setHeader("Clear-Site-Data", "\"cache\", \"cookies\", \"storage\", \"executionContexts\"");
    		return "redirect:/login-page"; 
    	}
		List<User> userList = userRepo.getUsersList();
		
		model.addAttribute("users", userList);
		return "user";
	}
	
	@PostMapping("/userAjax.htm")
	@ResponseBody
	public Map<String, Object> user(HttpServletRequest req) throws JsonMappingException, JsonProcessingException {
		Map<String, Object> response = new HashMap<>();
		String method = req.getParameter("method");
		User user =  new User();
		if(method.equals("getAllUser"))
		{
			List<User> userList = userRepo.getUsersList();
			response.put("userList", userList);
			
		}else if(method.equals("saveUser")){
			
			String userData = req.getParameter("userData");

			ObjectMapper mapper = new ObjectMapper();
			user = mapper.readValue(userData, User.class);
			String saveData = "Y";
			try {
				this.userRepo.saveUser(user);
			} catch (Exception e) {
				saveData = "N";
			}
			response.put("SaveData", saveData);
			
		}else if(method.equals("getIndividualUser"))
		{
			int userId = Integer.parseInt(req.getParameter("userId"));
			Optional<User> userObj = userRepo.getUserById(userId);
			response.put("user", userObj);
			
		}else if(method.equals("deleteUser"))
		{
			int userId = Integer.parseInt(req.getParameter("userId"));
			String saveData = "Y";
			try {
				userRepo.deleteUserById(userId);
			} catch (Exception e) {
				saveData = "N";
			}
			
			response.put("saveData", saveData);
			
		}else if(method.equals("checkUserIdAlreadyExsists"))
		{
			int userId = Integer.parseInt(req.getParameter("userId"));
			String saveData = "Y";
			try {
				Optional<User> userObj = userRepo.getUserById(userId);
				if(userObj.isPresent()) {
					saveData = "Y";
				}else {
					saveData = "N";
				}
				
			} catch (Exception e) {
//				saveData = "N";	
			}
			
			response.put("saveData", saveData);
			
		}else if(method.equals("checkUser"))
		{
			int userId = Integer.parseInt(req.getParameter("userId"));
			String password = req.getParameter("userPassword");

			Optional<User> userObj = userRepo.getUserById(userId);
			String exists;

			if (userObj.isPresent()) {
			    User u = userObj.get();
			    if (password != null && password.equals(u.getUserPassword())) {
			        exists = "Y";
			        
			        HttpSession session = req.getSession(true);
			        session.setAttribute("user", u); 
			    } else {
			        exists = "N";
			    }
			} else {
			    exists = "N";
			}

			response.put("exsists", exists);
			
		}
		
	    return response;
	}

}
