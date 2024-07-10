package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.CartProduct;

public class CartProductMapper implements RowMapper<CartProduct>{

	@Override
    public CartProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
		CartProduct product = new CartProduct();
        product.setProductName(rs.getString("product_name"));
        product.setProductImage(rs.getBytes("product_image"));
        product.setProductPrice(rs.getDouble("product_price"));
        return product;
    }
}
