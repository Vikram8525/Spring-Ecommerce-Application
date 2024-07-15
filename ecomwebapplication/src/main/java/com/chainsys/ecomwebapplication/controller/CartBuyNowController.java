package com.chainsys.ecomwebapplication.controller;

import com.chainsys.ecomwebapplication.dao.CartBuyNowDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartBuyNowController {

    @Autowired
    private CartBuyNowDAO cartBuyNowDAO;

    @PostMapping("/cartBuyNow")
    public String buyNow(@RequestParam("userId") int userId, 
                         @RequestParam("paymentMethod") String paymentMethod,  
                         Model model) {
        try {
            String checkBalanceResult = cartBuyNowDAO.checkWalletBalance(userId);
            if (!checkBalanceResult.equals("Success")) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", checkBalanceResult);
                return "OrderConfirmation.jsp";
            }

            String updateQuantitiesResult = cartBuyNowDAO.updateProductQuantities(userId);
            if (!updateQuantitiesResult.equals("Success")) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", updateQuantitiesResult);
                return "OrderConfirmation.jsp";
            }

            String orderNumber = cartBuyNowDAO.generateOrderNumber();
            String fillOrderDetailsResult = cartBuyNowDAO.fillOrderDetails(userId, orderNumber);
            if (!fillOrderDetailsResult.equals("Success")) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", fillOrderDetailsResult);
                return "OrderConfirmation.jsp";
            }

            String fillPaymentDetailsResult = cartBuyNowDAO.fillPaymentDetails(userId, orderNumber, paymentMethod);
            if (!fillPaymentDetailsResult.equals("Success")) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", fillPaymentDetailsResult);
                return "OrderConfirmation.jsp";
            }

            String updateWalletBalancesResult = cartBuyNowDAO.updateWalletBalances(userId, orderNumber);
            if (!updateWalletBalancesResult.equals("Success")) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", updateWalletBalancesResult);
                return "OrderConfirmation.jsp";
            }

            String markItemsAsBoughtResult = cartBuyNowDAO.markItemsAsBought(userId);
            if (!markItemsAsBoughtResult.equals("Success")) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", markItemsAsBoughtResult);
                return "OrderConfirmation.jsp";
            }

            boolean emailSent = cartBuyNowDAO.sendOrderEmails(userId, orderNumber);
            if (!emailSent) {
                model.addAttribute("status", "failure");
                model.addAttribute("message", "Order placed, but failed to send confirmation email.");
                return "OrderSuccess.jsp";
            }

            model.addAttribute("status", "success");
            model.addAttribute("message", "Order placed successfully!");
            model.addAttribute("orderNumber", orderNumber);
            return "OrderSuccess.jsp";
        } catch (Exception e) {
            model.addAttribute("status", "failure");
            model.addAttribute("message", "An error occurred while processing your order: " + e.getMessage());
            return "OrderConfirmation.jsp";
        }
    }
}
