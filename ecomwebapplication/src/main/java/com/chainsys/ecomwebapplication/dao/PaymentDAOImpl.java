package com.chainsys.ecomwebapplication.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.mapper.UserMapper1;
import com.chainsys.ecomwebapplication.model.User;

@Repository("paymentDAO")
public class PaymentDAOImpl implements PaymentDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
    public User getUserDetails(int userId) {
        String sql = "SELECT user_name, wallet_balance FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper1(), userId);
    }

	@Override
	public boolean addMoneyToWallet(int userId, double amount, String password) {
	        String sql = "UPDATE users SET wallet_balance = wallet_balance + ? WHERE user_id = ? AND password = ?";
	        int rowsUpdated = jdbcTemplate.update(sql, amount, userId, password);
	        return rowsUpdated > 0;
	    }
	
	@Override
	 public List<Map<String, Object>> getCartItems(int userId) {
        String sql = "SELECT c.*, p.product_name, p.category_name, p.product_price, p.product_quantity, u.user_name " +
                     "FROM Cart c " +
                     "JOIN Products p ON c.product_id = p.product_id " +
                     "JOIN Users u ON p.user_id = u.user_id " +
                     "WHERE c.user_id = ? AND c.is_bought = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("product_name", rs.getString("product_name"));
            item.put("product_category", rs.getString("category_name"));
            item.put("product_price", rs.getDouble("product_price"));
            item.put("quantity", rs.getInt("quantity"));
            item.put("seller_name", rs.getString("user_name"));
            item.put("total_price", rs.getDouble("product_price") * rs.getInt("quantity"));
            return item;
        }, new Object[]{userId});
    }

	
	@Override
    public Map<String, String> getDeliveryAddress(int userId) {
        String sql = "SELECT state, city, address, pincode FROM Users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Map<String, String> address = new HashMap<>();
            address.put("state", rs.getString("state"));
            address.put("city", rs.getString("city"));
            address.put("address", rs.getString("address"));
            address.put("pincode", rs.getString("pincode"));
            return address;
        }, new Object[]{userId});
    }
	
}
