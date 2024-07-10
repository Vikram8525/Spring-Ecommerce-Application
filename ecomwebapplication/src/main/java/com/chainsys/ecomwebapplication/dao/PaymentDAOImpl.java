package com.chainsys.ecomwebapplication.dao;

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
	
	
}
