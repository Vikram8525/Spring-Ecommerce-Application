package com.chainsys.ecomwebapplication.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.model.Product;
import com.chainsys.ecomwebapplication.model.ProductDetails;
import com.chainsys.ecomwebapplication.model.SellerDetails;

@Repository
public interface ProductDAO {

	public List<Product> getProductsForUser(int userId);
	
	public boolean addProduct(Product product);
	
	public boolean updateProductPrice(int productId, double newPrice);
	
	public boolean updateProductQuantity(int productId, int newQuantity);
	
	public boolean deleteProduct(int productId);
	
	public List<Product> getDeletedProductsForUser(int userId);
	
	public boolean undeleteProduct(int productId);
	
	public String processLogin(String userId, String password);
	
	public Map<Integer, String> getAllSellers();
	
	public List<Product> getFilteredProducts(String productType, double minPrice, double maxPrice, String searchValue);
	
	public int getBestSellerId(Map<Integer, Integer> sellerItemsSoldMap);
	
	public Map<Integer, Integer> getSellerItemsSold();
	
	public ProductDetails getProductDetails(int productId);
	
	public SellerDetails getSellerDetails(int userId);
	
	public boolean isBestSeller(int userId);
}
