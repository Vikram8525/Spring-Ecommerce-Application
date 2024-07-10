package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.WishlistProduct;

public class WishlistProductMapper implements RowMapper<WishlistProduct>{

	@Override
	public WishlistProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
		WishlistProduct product = new WishlistProduct();
        product.setProductName(rs.getString("product_name"));
        product.setProductImage(rs.getBytes("product_image"));
        return product;
    }

}
