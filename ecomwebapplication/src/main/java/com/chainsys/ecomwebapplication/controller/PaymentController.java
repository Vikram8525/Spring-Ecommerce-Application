package com.chainsys.ecomwebapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chainsys.ecomwebapplication.dao.PaymentDAO;

@Controller
public class PaymentController {

	@Autowired
	PaymentDAO paymentDAO;
	
	@PostMapping("/addMoney")
	public String addMoneyToWallet(
	        @RequestParam("userId") int userId,
	        @RequestParam("paymentMethod") String paymentMethod,
	        @RequestParam("amount") double amount,
	        @RequestParam("modalPassword") String password,
	        Model model) {

	    boolean validParameters = true;

	    if (amount <= 0) {
	        validParameters = false;
	    }

	    if (validParameters) {
	        boolean isSuccess = paymentDAO.addMoneyToWallet(userId, amount, password);
	        if (isSuccess) {
	            model.addAttribute("status", "success");
	            model.addAttribute("message", "Money added to wallet successfully!");
	        } else {
	            model.addAttribute("status", "error");
	            model.addAttribute("message", "Failed to add money to wallet. Please try again later.");
	        }
	    } else {
	        model.addAttribute("status", "error");
	        model.addAttribute("message", "Invalid amount. Please enter a positive value.");
	    }

	    return "Wallet.jsp?userId=" + userId + "&paymentMethod=" + paymentMethod;
	}

}
