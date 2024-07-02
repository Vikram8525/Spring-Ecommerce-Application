package com.chainsys.ecomwebapplication.controller;

import java.sql.SQLException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	                               RedirectAttributes redirectAttributes) {

	        User user = new User();
	        user.setUserId(userId);
	        user.setUserName(userName);
	        user.setUserEmail(userEmail);
	        user.setPassword(password);

	        boolean isUserAdded = userDAO.addUser(user);

	        if (isUserAdded) {
	            boolean isEmailSent = userDAO.sendWelcomeEmail(user);

	            if (isEmailSent) {
	                redirectAttributes.addFlashAttribute("status", "success");
	                redirectAttributes.addFlashAttribute("message", "Registration Successful!");
	                return "home.jsp";
	            } else {
	                redirectAttributes.addFlashAttribute("status", "failure");
	                redirectAttributes.addFlashAttribute("message", "Failed to send welcome email. Please try again.");
	                return "redirect:/registration?registration=email_failure";
	            }
	        } else {
	            redirectAttributes.addFlashAttribute("status", "failure");
	            redirectAttributes.addFlashAttribute("message", "Failed to register user. Please try again.");
	            return "redirect:/registration?registration=db_failure";
	        }
	    }
	 
	 @PostMapping("/login")
	    public String userLogin(HttpSession session, @RequestParam("userId") int userId, @RequestParam("password") String password, Model model) {
	        try {
	            User user = userDAO.getUserByIdAndPassword(userId, password);
	            if (user != null) {
	                session.setAttribute("user", user);
	                return "home.jsp";
	            } else {
	                model.addAttribute("status", "failed");
	                return "LoginForm.jsp";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("status", "error");
	            return "redirect:/LoginForm.jsp";
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
}
