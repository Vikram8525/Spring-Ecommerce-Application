package com.chainsys.ecomwebapplication.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface CartBuyNowDAO {

	 String checkWalletBalance(int userId);

	    String updateProductQuantities(int userId);

	    String fillOrderDetails(int userId, String orderNumber);

	    String fillPaymentDetails(int userId, String orderNumber, String paymentMethod);

	    String updateWalletBalances(int userId, String orderNumber);

	    String generateOrderNumber();

	    String markItemsAsBought(int userId);

	    boolean sendOrderEmails(int userId, String orderNumber);
}
