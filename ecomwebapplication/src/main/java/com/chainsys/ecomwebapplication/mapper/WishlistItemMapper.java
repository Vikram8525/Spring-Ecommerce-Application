package com.chainsys.ecomwebapplication.mapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.WishlistItem;

public class WishlistItemMapper implements RowMapper<WishlistItem>{

	@Override
	public WishlistItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		 WishlistItem item = new WishlistItem();
         item.setWishlistId(rs.getInt("wishlist_id"));
         item.setProductId(rs.getInt("product_id"));
         item.setProductName(rs.getString("product_name"));
         item.setProductPrice(rs.getDouble("product_price"));
         item.setProductQuantity(rs.getInt("product_quantity"));
         
         Blob blob = rs.getBlob("product_image");
         byte[] imageBytes = blob.getBytes(1, (int) blob.length());
         String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
         item.setProductImage(base64Image);
         
         return item;
	}

}
