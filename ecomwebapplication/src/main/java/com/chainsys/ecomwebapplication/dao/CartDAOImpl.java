package com.chainsys.ecomwebapplication.dao;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.mapper.CartItemMapper;
import com.chainsys.ecomwebapplication.mapper.CartProductMapper;
import com.chainsys.ecomwebapplication.mapper.WishlistItem2Mapper;
import com.chainsys.ecomwebapplication.mapper.WishlistItemMapper;
import com.chainsys.ecomwebapplication.mapper.WishlistProductMapper;
import com.chainsys.ecomwebapplication.model.CartItem;
import com.chainsys.ecomwebapplication.model.CartProduct;
import com.chainsys.ecomwebapplication.model.WishlistItem;
import com.chainsys.ecomwebapplication.model.WishlistItem2;
import com.chainsys.ecomwebapplication.model.WishlistProduct;

@Repository("cartDAO")
public class CartDAOImpl implements CartDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void addToCart(int userId, int productId) {
	    String selectProductQuery = "SELECT product_name, product_image, product_price FROM products WHERE product_id = ?";
	    String checkCartQuery = "SELECT * FROM cart WHERE user_id = ? AND product_id = ? AND is_bought = 0";
	    String updateCartQuery = "UPDATE cart SET quantity = ?, total_price = ? WHERE user_id = ? AND product_id = ?";
	    String insertCartQuery = "INSERT INTO cart (user_id, product_id, product_name, product_image, quantity, date_added, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    CartProduct product = jdbcTemplate.queryForObject(selectProductQuery, new CartProductMapper(), productId);
	    List<CartItem> cartItems = jdbcTemplate.query(checkCartQuery, new CartItemMapper(), userId, productId);

	    double totalPrice = product.getProductPrice();

	    if (!cartItems.isEmpty()) {
	        CartItem cartItem = cartItems.get(0);
	        int newQuantity = cartItem.getQuantity() + 1;
	        jdbcTemplate.update(updateCartQuery, newQuantity, totalPrice * newQuantity, userId, productId);
	    } else {
	        jdbcTemplate.update(insertCartQuery, userId, productId, product.getProductName(), product.getProductImage(), 1, new Timestamp(System.currentTimeMillis()), totalPrice);
	    }
	}

	@Override
	public List<Map<String, Object>> getCartItemsForUser(int userId) {
	    String cartItemsQuery = "SELECT c.*, p.product_name, p.product_image, p.product_price, p.product_quantity " +
	                            "FROM Cart c JOIN Products p ON c.product_id = p.product_id WHERE c.user_id = ? AND c.is_bought = 0";

	    List<Map<String, Object>> cartItems = new ArrayList<>();
	    List<Map<String, Object>> rows = jdbcTemplate.queryForList(cartItemsQuery, userId);
	    
	    for (Map<String, Object> row : rows) {
	        Map<String, Object> item = new HashMap<>();
	        item.put("cart_id", row.get("cart_id"));
	        item.put("product_id", row.get("product_id"));
	        item.put("product_name", row.get("product_name"));
	        item.put("product_price", row.get("product_price"));
	        item.put("product_quantity", row.get("product_quantity"));
	        item.put("quantity", row.get("quantity"));

	        byte[] imageBytes = (byte[]) row.get("product_image");
	        if (imageBytes != null) {
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	            item.put("product_image", base64Image);
	        } else {
	            item.put("product_image", "");
	        }

	        cartItems.add(item);
	    }

	    return cartItems;
	}

	
	@Override
    public boolean deleteCartItem(int cartId) {
        String deleteQuery = "DELETE FROM Cart WHERE cart_id = ?";
        int rowsDeleted = jdbcTemplate.update(deleteQuery, cartId);
        return rowsDeleted > 0;
    }
	
	@Override
	 public boolean updateCartItemQuantity(int cartId, int newQuantity) {
	        String updateQuery = "UPDATE Cart SET quantity = ? WHERE cart_id = ?";
	        try {
	            int rowsUpdated = jdbcTemplate.update(updateQuery, newQuantity, cartId);
	            return rowsUpdated > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	
	
	@Override
	public List<WishlistItem> getWishlistItems(int userId) {
        String query = "SELECT w.*, p.product_name, p.product_image, p.product_price, p.product_quantity " +
                       "FROM Wishlist w JOIN Products p ON w.product_id = p.product_id WHERE w.user_id = ?";
        return jdbcTemplate.query(query, new WishlistItemMapper(), userId);
    }
	
	
	@Override
	public void addToWishlist(int userId, int productId) {
        String selectProductQuery = "SELECT product_name, product_image FROM products WHERE product_id = ?";
        String checkWishlistQuery = "SELECT * FROM wishlist WHERE user_id = ? AND product_id = ?";
        String insertWishlistQuery = "INSERT INTO wishlist (user_id, product_id, product_name, product_image) VALUES (?, ?, ?, ?)";

        WishlistProduct product = jdbcTemplate.queryForObject(selectProductQuery, new WishlistProductMapper(), productId);

        List<WishlistItem2> wishlistItem2 = jdbcTemplate.query(checkWishlistQuery, new WishlistItem2Mapper(), userId, productId);

        if (wishlistItem2.isEmpty()) {
            jdbcTemplate.update(insertWishlistQuery, userId, productId, product.getProductName(), product.getProductImage());
        }
	}
	
	 
	@Override
	 public boolean deleteWishlist(int wishlistId) {
	        String deleteQuery = "DELETE FROM Wishlist WHERE wishlist_id = ?";
	        int rowsDeleted = jdbcTemplate.update(deleteQuery, wishlistId);
	        return rowsDeleted > 0;
	    }
}
