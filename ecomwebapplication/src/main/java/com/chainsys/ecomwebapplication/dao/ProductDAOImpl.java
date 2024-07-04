package com.chainsys.ecomwebapplication.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.mapper.ProductMapper;
import com.chainsys.ecomwebapplication.model.Product;

@Repository("productDAO")
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Product> getProductsForUser(int userId) {
		String sql = "SELECT * FROM Products WHERE user_id = ? AND is_deleted = 0";
		List<Product> products = jdbcTemplate.query(sql, new ProductMapper(), userId);
		return products;
	}

	@Override
	public boolean addProduct(Product product) {
		String sql = "INSERT INTO products (user_id, seller_name, category_name, product_name, product_image, sample_image, left_image, right_image, bottom_image, product_price, product_description, product_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			int rowsInserted = jdbcTemplate.update(sql, product.getUserId(), product.getSellerName(),
					product.getCategoryName(), product.getProductName(), product.getProductImage(),
					product.getSampleImage(), product.getLeftImage(), product.getRightImage(), product.getBottomImage(),
					product.getProductPrice(), product.getProductDescription(), product.getProductQuantity());

			return rowsInserted > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateProductPrice(int productId, double newPrice) {
        String sql = "UPDATE Products SET product_price = ? WHERE product_id = ?";
        
        try {
            int rowsUpdated = jdbcTemplate.update(sql, newPrice, productId);
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	@Override
    public boolean updateProductQuantity(int productId, int newQuantity) {
        String sql = "UPDATE Products SET product_quantity = ? WHERE product_id = ?";
        try {
            int rowsUpdated = jdbcTemplate.update(sql, newQuantity, productId);
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	 @Override
	    public boolean deleteProduct(int productId) {
	        String sql = "UPDATE Products SET is_deleted = '1' WHERE product_id = ?";
	        try {
	            int rowsDeleted = jdbcTemplate.update(sql, productId);
	            return rowsDeleted > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 
	 @Override
	 public List<Product> getDeletedProductsForUser(int userId) {
		    String sql = "SELECT * FROM Products WHERE user_id = ? AND is_deleted = 1";
		    List<Product> products = jdbcTemplate.query(sql, new ProductMapper(), userId);
		    return products;
		}
	 
	 @Override
	    public boolean undeleteProduct(int productId) {
	        String sql = "UPDATE Products SET is_deleted = '0' WHERE product_id = ?";
	        try {
	            int rowsUpdated = jdbcTemplate.update(sql, productId);
	            return rowsUpdated > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


}
