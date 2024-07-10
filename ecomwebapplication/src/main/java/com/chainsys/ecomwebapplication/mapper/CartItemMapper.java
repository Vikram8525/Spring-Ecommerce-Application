package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.CartItem;

public class CartItemMapper implements RowMapper<CartItem>{

	@Override
    public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setUserId(rs.getInt("user_id"));
        cartItem.setProductId(rs.getInt("product_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setTotalPrice(rs.getDouble("total_price"));
        return cartItem;
	}
}
