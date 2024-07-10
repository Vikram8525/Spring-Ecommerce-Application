package com.chainsys.ecomwebapplication.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.model.WishlistItem;

@Repository
public interface CartDAO {

	public void addToCart(int userId, int productId);
	
	public List<Map<String, Object>> getCartItemsForUser(int userId);
	
	public boolean deleteCartItem(int cartId);
	
	 public boolean updateCartItemQuantity(int cartId, int newQuantity);
	 
	 public List<WishlistItem> getWishlistItems(int userId);
	 
	 public void addToWishlist(int userId, int productId);
	 
	 public boolean deleteWishlist(int wishlistId);
}
