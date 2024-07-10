package com.chainsys.ecomwebapplication.dao;

import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.model.User;

@Repository
public interface PaymentDAO {

	public User getUserDetails(int userId);
	
	public boolean addMoneyToWallet(int userId, double amount,String password);
}
