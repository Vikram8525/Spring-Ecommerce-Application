package com.chainsys.ecomwebapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chainsys.ecomwebapplication.dao.CartDAO;
import com.chainsys.ecomwebapplication.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	CartDAO cartDAO;
	
	@PostMapping("/addToCart")
    public String addToCart(HttpSession session, @RequestParam("productId") int productId) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "LoginForm.jsp";
        }

        int userId = currentUser.getUserId();
        cartDAO.addToCart(userId, productId);

        return "ViewProduct.jsp";
    }
	
	@PostMapping("/deleteCartItem")
    public String deleteCartItem(@RequestParam("cartId") int cartId, Model model) {
        boolean deletionSuccess = cartDAO.deleteCartItem(cartId);
        if (deletionSuccess) {
        	model.addAttribute("status", "success");
            model.addAttribute("message", "Item removed successfully.");
        } else {
        	model.addAttribute("status", "failure");
            model.addAttribute("message", "Failed to remove item.");
        }
        return "ViewCart.jsp"; 
    }
	
	@PostMapping("/updateCartItem")
    public String updateCartItem(@RequestParam("cartId") int cartId,
                                 @RequestParam("newQuantity") int newQuantity,
                                 Model model) {
        boolean updateSuccess = cartDAO.updateCartItemQuantity(cartId, newQuantity);
        if (updateSuccess) {
            model.addAttribute("status", "succes");
            model.addAttribute("message", "Quantity updated successfully.");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "Failed to update quantity.");
        }
        return "ViewCart.jsp";
    }
    
	
	@PostMapping("/addToWishlist")
    public String addToWishlist(HttpSession session, @RequestParam("productId") int productId) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:LoginForm.jsp";
        }

        int userId = currentUser.getUserId();
        cartDAO.addToWishlist(userId, productId);

        return "redirect:ViewProduct.jsp";
    }
	
	@PostMapping("/removeFromWishlist")
    public String removeFromWishlist(@RequestParam("wishlistId") int wishlistId, Model model) {
        try {
            boolean deletionSuccess = cartDAO.deleteWishlist(wishlistId);
            if (deletionSuccess) {
                model.addAttribute("status", "success");
                model.addAttribute("message", "Item removed from wishlist successfully.");
            } else {
                model.addAttribute("status", "fail");
                model.addAttribute("message", "Failed to remove item from wishlist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("status", "fail");
            model.addAttribute("message", "An error occurred while removing the item from the wishlist.");
        }
        return "ViewWishlist.jsp";
    }
}
