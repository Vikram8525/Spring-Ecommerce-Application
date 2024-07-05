package com.chainsys.ecomwebapplication.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.mapper.ProductDetailsMapper;
import com.chainsys.ecomwebapplication.mapper.ProductMapper;
import com.chainsys.ecomwebapplication.mapper.SellerDetailsMapper;
import com.chainsys.ecomwebapplication.model.Product;
import com.chainsys.ecomwebapplication.model.ProductDetails;
import com.chainsys.ecomwebapplication.model.SellerDetails;

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
	 
	 @Override
	 public String processLogin(String userId, String password) {
		    String selectQuery = "SELECT * FROM users WHERE user_id = ? AND password = ?";

		    try {
		        List<Map<String, Object>> result = jdbcTemplate.queryForList(selectQuery, userId, password);

		        if (!result.isEmpty()) {
		            Map<String, Object> row = result.get(0);
		            Boolean isSeller = (Boolean) row.get("is_seller");

		            if (Boolean.TRUE.equals(isSeller)) {
		                return "success";
		            } else {
		                String updateQuery = "UPDATE users SET is_seller = 1 WHERE user_id = ?";
		                int rowsUpdated = jdbcTemplate.update(updateQuery, userId);

		                if (rowsUpdated > 0) {
		                    return "success";
		                } else {
		                    return "error";
		                }
		            }
		        } else {
		            return "failed";
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        return "error";
		    }
		}

	

	
	 //Start of the code change 
	 
	 @Override
	 public Map<Integer, String> getAllSellers() {
	        String query = "SELECT user_id, user_name, num_items_sold FROM Users";
	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	        Map<Integer, String> sellerMap = new HashMap<>();
	        for (Map<String, Object> row : rows) {
	            Integer userId = (Integer) row.get("user_id");
	            String userName = (String) row.get("user_name");
	            sellerMap.put(userId, userName);
	        }
	        return sellerMap;
	    }
	 	
	 	@Override
	    public List<Product> getFilteredProducts(String productType, double minPrice, double maxPrice, String searchValue) {
	        StringBuilder query = new StringBuilder("SELECT * FROM Products WHERE product_price BETWEEN ? AND ? AND is_deleted = 0 ");
	        List<Object> params = new ArrayList<>();
	        params.add(minPrice);
	        params.add(maxPrice);

	        if (productType != null && !productType.isEmpty()) {
	            query.append(" AND category_name = ?");
	            params.add(productType);
	        }

	        if (searchValue != null && !searchValue.isEmpty()) {
	            query.append(" AND product_name LIKE ?");
	            params.add("%" + searchValue + "%");
	        }

	        return jdbcTemplate.query(query.toString(),(rs, rowNum) -> {
	            Product product = new Product();
	            product.setProductId(rs.getInt("product_id"));
	            product.setProductName(rs.getString("product_name"));
	            product.setProductPrice(rs.getDouble("product_price"));
	            product.setProductImage(rs.getBytes("product_image"));
	            product.setUserId(rs.getInt("user_id"));
	            return product;
	        },params.toArray());
	    }
	    
	    @Override
	    public int getBestSellerId(Map<Integer, Integer> sellerItemsSoldMap) {
	        return sellerItemsSoldMap.entrySet().stream()
	            .max(Map.Entry.comparingByValue())
	            .map(Map.Entry::getKey)
	            .orElse(-1);
	    }
	    
	    
	    @Override
	    public Map<Integer, Integer> getSellerItemsSold() {
	        String query = "SELECT user_id, num_items_sold FROM Users";
	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

	        Map<Integer, Integer> sellerItemsSoldMap = new HashMap<>();
	        for (Map<String, Object> row : rows) {
	            Integer userId = (Integer) row.get("user_id");
	            Integer numItemsSold = (Integer) row.get("num_items_sold");
	            sellerItemsSoldMap.put(userId, numItemsSold);
	        }
	        return sellerItemsSoldMap;
	    }
	    
	    //Start of the code 
	    public ProductDetails getProductDetails(int productId) {
	        String getProductQuery = "SELECT * FROM Products WHERE product_id = ? AND is_deleted = 0";
	        return jdbcTemplate.queryForObject(getProductQuery, new ProductDetailsMapper(), new Object[] { productId });
	    }

	    public SellerDetails getSellerDetails(int userId) {
	        String getSellerQuery = "SELECT * FROM Users WHERE user_id = ?";
	        return jdbcTemplate.queryForObject(getSellerQuery, new SellerDetailsMapper(), new Object[] { userId });
	    }

	    public boolean isBestSeller(int userId) {
	        String getMaxItemsSoldQuery = "SELECT MAX(num_items_sold) AS max_sold FROM Users";
	        String getUserItemsSoldQuery = "SELECT num_items_sold FROM Users WHERE user_id = ?";

	        Integer maxItemsSold = jdbcTemplate.queryForObject(getMaxItemsSoldQuery, Integer.class);
	        Integer userItemsSold = jdbcTemplate.queryForObject(getUserItemsSoldQuery,Integer.class, new Object[] { userId });

	        return userItemsSold != null && maxItemsSold != null && userItemsSold.equals(maxItemsSold);
	    }
}
