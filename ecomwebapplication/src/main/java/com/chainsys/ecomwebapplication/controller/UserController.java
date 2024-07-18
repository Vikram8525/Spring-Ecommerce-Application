package com.chainsys.ecomwebapplication.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chainsys.ecomwebapplication.dao.UserDAO;
import com.chainsys.ecomwebapplication.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	
	@Autowired
    private UserDAO userDAO;

	@RequestMapping("/")
	public String index() {

		return "home.jsp";
	}

	@GetMapping("/login")
	public String login() {
		return "LoginForm.jsp";
	}

	@GetMapping("/register")
	public String register() {
		return "RegistrationForm.jsp";
	}

	 @GetMapping("/generateUserId")
	    @ResponseBody
	    public Map<String, Integer> generateUserId() {
	        int userId = userDAO.generateUniqueUserId();
	        Map<String, Integer> response = new HashMap<>();
	        response.put("userId", userId);
	        return response;
	    }
	 
	 @PostMapping("/registration")
	 public String registerUser(@RequestParam("user_id") int userId,
	                            @RequestParam("user_name") String userName,
	                            @RequestParam("user_email") String userEmail,
	                            @RequestParam("password") String password,
	                            Model model) {

		 if (userDAO.isUserExists(userEmail,userId)) {
		        model.addAttribute("status", "failure");
		        model.addAttribute("message", "User already exists with this E-Mail.");
		        return "RegistrationForm.jsp";
		    }
	     User user = new User();
	     user.setUserId(userId);
	     user.setUserName(userName);
	     user.setUserEmail(userEmail);
	     user.setPassword(password);

	     boolean isUserAdded = userDAO.addUser(user);

	     if (isUserAdded) {
	         boolean isEmailSent = userDAO.sendWelcomeEmail(user);

	         if (isEmailSent) {
	             model.addAttribute("status", "success");
	             model.addAttribute("message", "Registration Successful!");
	             model.addAttribute("user_id", userId);
	             return "home.jsp";
	         } else {
	             model.addAttribute("status", "failure");
	             model.addAttribute("message", "Failed to send welcome email. Please try again.");
	             return "RegistrationForm.jsp";
	         }
	     } else {
	         model.addAttribute("status", "failure");
	         model.addAttribute("message", "Failed to register user. Please try again.");
	         return "RegistrationForm.jsp";
	     }
	 }

	 
	 @PostMapping("/login")
	 public String userLogin(HttpSession session,
	                         @RequestParam("userId") int userId,
	                         @RequestParam("password") String password,
	                         Model model) {
	     try {
	         User user = userDAO.getUserByIdAndPassword(userId, password);
	         if (user != null) {
	             session.setAttribute("user", user);
	             return "home.jsp"; 
	         } else {
	             model.addAttribute("status", "failed"); 
	             return "LoginForm.jsp";
	         }
	     } catch (EmptyResultDataAccessException e) {
	         model.addAttribute("status", "failed");
	         model.addAttribute("message", "Incorrect username or password.");
	         return "LoginForm.jsp";
	     } catch (DataAccessException e) {
	         e.printStackTrace(); 
	         model.addAttribute("status", "error");
	         model.addAttribute("message", "Error accessing database. Please try again later.");
	         return "LoginForm.jsp";
	     } catch (Exception e) {
	         e.printStackTrace(); 
	         model.addAttribute("status", "error");
	         model.addAttribute("message", "An unexpected error occurred. Please try again later.");
	         return "LoginForm.jsp";
	     }
	 }

	 @PostMapping("/logout")
	 public String userLogout(HttpSession session, HttpServletRequest request) {
		 session = request.getSession(false);
		 if(session != null) {
			 session.invalidate();
		 }
		 
		 return "redirect:/home.jsp";
	 }
	 
	 	@PostMapping("/profile")
	    public String updateUser(@RequestParam("user_id") int userId,
	                               @RequestParam("first_name") String firstName,
	                               @RequestParam("last_name") String lastName,
	                               @RequestParam("address") String address,
	                               @RequestParam("state") String state,
	                               @RequestParam("city") String city,
	                               @RequestParam("pincode") String pincode,
	                               Model model) {

	        User user = new User();
	        user.setUserId(userId);
	        user.setFirstName(firstName);
	        user.setLastName(lastName);
	        user.setAddress(address);
	        user.setState(state);
	        user.setCity(city);
	        user.setPincode(pincode);
	        
	 	boolean updateSuccess = userDAO.updateUser(user);

        if (updateSuccess) {
            model.addAttribute("statuss", "successs");
            model.addAttribute("message", "Profile updated successfully!");
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Failed to update profile. Please try again later.");
        }

        return "home.jsp"; 
    }
}
