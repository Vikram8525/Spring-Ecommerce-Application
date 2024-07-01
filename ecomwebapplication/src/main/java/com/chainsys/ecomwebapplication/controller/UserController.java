package com.chainsys.ecomwebapplication.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chainsys.ecomwebapplication.dao.UserDAO;
import com.chainsys.ecomwebapplication.model.User;

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

	        User user = new User();
	        user.setUserId(userId);
	        user.setUserName(userName);
	        user.setUserEmail(userEmail);
	        user.setPassword(password);

	        boolean isUserAdded = userDAO.addUser(user);

	        if (isUserAdded) {
	            boolean isEmailSent = userDAO.sendWelcomeEmail(user);

	            if (isEmailSent) {
	                // Set attributes for success status and message
	                model.addAttribute("status", "success");
	                model.addAttribute("message", "Registration Successful!");

	                // Redirect to home.jsp with success status
	                return "redirect:/home";
	            } else {
	                // Handle email sending failure
	                model.addAttribute("status", "failure");
	                model.addAttribute("message", "Failed to send welcome email. Please try again.");
	                return "redirect:/registration?registration=email_failure";
	            }
	        } else {
	            // Handle user insertion failure
	            model.addAttribute("status", "failure");
	            model.addAttribute("message", "Failed to register user. Please try again.");
	            return "redirect:/registration?registration=db_failure";
	        }
	    }
}
