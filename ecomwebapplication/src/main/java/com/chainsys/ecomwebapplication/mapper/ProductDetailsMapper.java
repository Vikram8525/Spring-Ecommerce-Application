package com.chainsys.ecomwebapplication.mapper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;

import com.chainsys.ecomwebapplication.model.ProductDetails;

public class ProductDetailsMapper implements RowMapper<ProductDetails>  {
	@Override
    public ProductDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductDetails details = new ProductDetails();
        details.productName = rs.getString("product_name");
        details.productDescription = rs.getString("product_description");
        details.productPrice = rs.getDouble("product_price");
        details.productQuantity = rs.getInt("product_quantity");
        details.userId = rs.getInt("user_id");
        details.imageBase64List = new ArrayList<>();

        for (String column : new String[] { "product_image", "sample_image", "left_image", "right_image",
                "bottom_image" }) {
            Blob blob = rs.getBlob(column);
            if (blob != null) {
                byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                details.imageBase64List.add(base64Image);
            }
        }
        return details;
    }
}
