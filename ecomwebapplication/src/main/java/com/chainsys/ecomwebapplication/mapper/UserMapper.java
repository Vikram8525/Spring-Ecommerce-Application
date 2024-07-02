package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.User;

public class UserMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		 
		
		int id = rs.getInt("user_id");
         String userName = rs.getString("user_name");
         String userEmail = rs.getString("user_email");
         String userPassword = rs.getString("password");
         Timestamp createdAt = rs.getTimestamp("created_at");
         Timestamp updatedAt = rs.getTimestamp("updated_at");
         boolean isSeller = rs.getBoolean("is_seller");
         boolean isDeleted = rs.getBoolean("is_deleted");
         int numItemsBought = rs.getInt("num_items_bought");
         int numItemsSold = rs.getInt("num_items_sold");
         double walletBalance = rs.getDouble("wallet_balance");
         String firstName = rs.getString("first_name");
         String lastName = rs.getString("last_name");
         String address = rs.getString("address");
         String state = rs.getString("state");
         String city = rs.getString("city");
         String pincode = rs.getString("pincode");
         
         User user = new User();
         
         user.setUserId(id);
         user.setUserName(userName);
         user.setUserEmail(userEmail);
         user.setPassword(userPassword);
         user.setCreatedAt(createdAt); 
         user.setUpdatedAt(updatedAt); 
         user.setSeller(isSeller); 
         user.setDeleted(isDeleted); 
         user.setNumItemsBought(numItemsBought); 
         user.setNumItemsSold(numItemsSold); 
         user.setWalletBalance(walletBalance); 
         user.setFirstName(firstName);
         user.setLastName(lastName);
         user.setAddress(address);
         user.setState(state);
         user.setCity(city);
         user.setPincode(pincode);
         
		return user;
	}

}
