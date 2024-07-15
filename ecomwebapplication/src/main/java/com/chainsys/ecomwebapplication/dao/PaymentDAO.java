package com.chainsys.ecomwebapplication.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.model.User;

@Repository
public interface PaymentDAO {

	public User getUserDetails(int userId);
	
	public boolean addMoneyToWallet(int userId, double amount,String password);
	
	public List<Map<String, Object>> getCartItems(int userId);
	
	public Map<String, String> getDeliveryAddress(int userId);
}
