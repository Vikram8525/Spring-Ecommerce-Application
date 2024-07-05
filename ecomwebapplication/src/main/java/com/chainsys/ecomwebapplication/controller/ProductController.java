package com.chainsys.ecomwebapplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chainsys.ecomwebapplication.dao.ProductDAO;
import com.chainsys.ecomwebapplication.model.Product;

@Controller
public class ProductController {

	@Autowired
	ProductDAO productDAO;

	
	@PostMapping("/addProduct")
    public String addProduct(@RequestParam("user_id") int userId,
                             @RequestParam("seller_name") String sellerName,
                             @RequestParam("category_name") String categoryName,
                             @RequestParam("product_name") String productName,
                             @RequestParam("product_image") MultipartFile productImage,
                             @RequestParam("sample_image") MultipartFile sampleImage,
                             @RequestParam("left_image") MultipartFile leftImage,
                             @RequestParam("right_image") MultipartFile rightImage,
                             @RequestParam("bottom_image") MultipartFile bottomImage,
                             @RequestParam("product_price") double productPrice,
                             @RequestParam("product_description") String productDescription,
                             @RequestParam("product_quantity") int productQuantity,
                             Model model) {

        try {
            Product product = new Product();
            product.setUserId(userId);
            product.setSellerName(sellerName);
            product.setCategoryName(categoryName);
            product.setProductName(productName);
            product.setProductImage(productImage.getBytes());
            product.setSampleImage(sampleImage.getBytes());
            product.setLeftImage(leftImage.getBytes());
            product.setRightImage(rightImage.getBytes());
            product.setBottomImage(bottomImage.getBytes());
            product.setProductPrice(productPrice);
            product.setProductDescription(productDescription);
            product.setProductQuantity(productQuantity);

            boolean added = productDAO.addProduct(product);
            
            if (added) {
                model.addAttribute("statu", "successss");
                model.addAttribute("message", "Product added successfully!");
            } else {
                model.addAttribute("statu", "error");
                model.addAttribute("message", "Failed to add product.");
            }
        } catch (Exception e) {
            model.addAttribute("statu", "error");
            model.addAttribute("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }

        return "home.jsp"; 
    }
	
	 @PostMapping("/updateProductPrice")
	    public String updateProductPrice(
	            @RequestParam("productId") int productId,
	            @RequestParam("newPrice") double newPrice,
	            Model model) {

	        try {
	            boolean success = productDAO.updateProductPrice(productId, newPrice);

	            if (success) {
	                model.addAttribute("status", "success");
	                model.addAttribute("message", "Product price updated successfully!");
	            } else {
	                model.addAttribute("status", "error");
	                model.addAttribute("message", "Failed to update product price.");
	            }
	        } catch (Exception e) {
	            model.addAttribute("status", "error");
	            model.addAttribute("message", "Error: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return "SellerViewProducts.jsp"; 
	    }
	
	 @PostMapping("/updateProductQuantity")
	    public String updateProductQuantity(
	            @RequestParam("productId") int productId,
	            @RequestParam("newQuantity") int newQuantity,
	            Model model) {

	        try {
	            boolean success = productDAO.updateProductQuantity(productId, newQuantity);
	            if (success) {
	                model.addAttribute("status", "success");
	                model.addAttribute("message", "Stock added successfully!");
	            } else {
	                model.addAttribute("status", "failed");
	                model.addAttribute("message", "Failed to add stock!");
	            }
	        } catch (Exception e) {
	            model.addAttribute("status", "failed");
	            model.addAttribute("message", "Error: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return "SellerViewProducts.jsp";
	    }
	 
	 
	 @PostMapping("/deleteProduct")
	    public String deleteProduct(
	            @RequestParam("productId") int productId,
	            Model model) {

	        try {
	            boolean success = productDAO.deleteProduct(productId);
	            if (success) {
	                model.addAttribute("status", "deleted");
	                model.addAttribute("message", "Product deleted successfully!");
	            } else {
	                model.addAttribute("status", "failed");
	                model.addAttribute("message", "Failed to delete the product!");
	            }
	        } catch (Exception e) {
	            model.addAttribute("status", "failed");
	            model.addAttribute("message", "Error: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return "SellerViewProducts.jsp";
	    }
	 
	 
	 @PostMapping("/undeleteProduct")
	    public String undeleteProduct(
	            @RequestParam("productId") int productId,
	            Model model) {

	        try {
	            boolean success = productDAO.undeleteProduct(productId);

	            if (success) {
	                model.addAttribute("status", "success");
	                model.addAttribute("message", "Product added again successfully!");
	            } else {
	                model.addAttribute("status", "error");
	                model.addAttribute("message", "Failed to add product again.");
	            }
	        } catch (Exception e) {
	            model.addAttribute("status", "error");
	            model.addAttribute("message", "Error: " + e.getMessage());
	            e.printStackTrace();
	        }

	        return "SellerViewProducts.jsp";
	    }
	 
	 
	 @PostMapping("/sellerlogin")
	    public String processLogin(
	            @RequestParam("userId") String userId,
	            @RequestParam("password") String password,
	            Model model) {

	        try {
	            String result = productDAO.processLogin(userId, password);

	            if ("success".equals(result)) {
	                model.addAttribute("stat", "success");
	                model.addAttribute("message", "You have successfully became a part of us!");
	                return "home.jsp";
	            } else if ("failed".equals(result)) {
	                model.addAttribute("stat", "failed");
	                model.addAttribute("message", "Login failed. Please check your credentials.");
	                return "SellerLogin.jsp";
	            } else {
	                model.addAttribute("stat", "error");
	                model.addAttribute("message", "Unknown error occurred.");
	                return "SellerLogin.jsp";
	            }
	        } catch (Exception e) {
	            model.addAttribute("stat", "error");
	            model.addAttribute("message", "Error: " + e.getMessage());
	            e.printStackTrace();
	            return "SellerLogin.jsp";
	        }
	    }
	 
	 
}
