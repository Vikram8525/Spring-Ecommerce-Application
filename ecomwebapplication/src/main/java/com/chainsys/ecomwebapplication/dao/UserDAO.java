package com.chainsys.ecomwebapplication.dao;

import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.model.User;

@Repository
public interface UserDAO {

	
	public boolean addUser(User user);

	public boolean updateUser(User user);

	public int generateUniqueUserId();

	public boolean sendWelcomeEmail(User user);

	public User getUserDetailsFromDatabase(int userId);
	
	public User getUserByIdAndPassword(int userId, String password);

	public boolean isUserExists(String userEmail, int userId);

	public int getCartItemCount(User user);

	public int getWishlistItemCount(User user);
}
