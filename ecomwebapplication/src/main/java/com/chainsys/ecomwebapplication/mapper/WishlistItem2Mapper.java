package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.WishlistItem2;

public class WishlistItem2Mapper implements RowMapper<WishlistItem2>{

	@Override
	public WishlistItem2 mapRow(ResultSet rs, int rowNum) throws SQLException {
		WishlistItem2 item = new WishlistItem2();
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        return item;
    }
	

}
