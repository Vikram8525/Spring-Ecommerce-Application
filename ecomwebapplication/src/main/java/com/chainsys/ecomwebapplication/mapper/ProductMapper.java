package com.chainsys.ecomwebapplication.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.Product;

public class ProductMapper implements RowMapper<Product> {

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		int productId = rs.getInt("product_id");
        int userId = rs.getInt("user_id");
        String sellerName = rs.getString("seller_name");
        String categoryName = rs.getString("category_name");
        String productName = rs.getString("product_name");
        byte[] productImage = rs.getBytes("product_image");
        byte[] sampleImage = rs.getBytes("sample_image");
        byte[] leftImage = rs.getBytes("left_image");
        byte[] rightImage = rs.getBytes("right_image");
        byte[] bottomImage = rs.getBytes("bottom_image");
        double productPrice = rs.getDouble("product_price");
        String productDescription = rs.getString("product_description");
        int productQuantity = rs.getInt("product_quantity");
        boolean isDeleted = rs.getBoolean("is_deleted");
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        

        Product product = new Product();
        
        product.setProductId(productId);
        product.setUserId(userId);
        product.setSellerName(sellerName);
        product.setCategoryName(categoryName);
        product.setProductName(productName);
        product.setProductImage(productImage);
        product.setSampleImage(sampleImage);
        product.setLeftImage(leftImage);
        product.setRightImage(rightImage);
        product.setBottomImage(bottomImage);
        product.setProductPrice(productPrice);
        product.setProductDescription(productDescription);
        product.setProductQuantity(productQuantity);
        product.setDeleted(isDeleted);
        product.setCreatedAt(createdAt);
        product.setUpdatedAt(updatedAt);

        return product;
	}
}