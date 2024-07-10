package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.User;

public class UserMapper1 implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
        user.setUserName(rs.getString("user_name"));
        user.setWalletBalance(rs.getDouble("wallet_balance"));
        return user;
    }

}
