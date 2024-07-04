package com.chainsys.ecomwebapplication.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.model.Product;

@Repository
public interface ProductDAO {

	public List<Product> getProductsForUser(int userId);
	
	public boolean addProduct(Product product);
	
	public boolean updateProductPrice(int productId, double newPrice);
	
	public boolean updateProductQuantity(int productId, int newQuantity);
	
	public boolean deleteProduct(int productId);
	
	public List<Product> getDeletedProductsForUser(int userId);
	
	public boolean undeleteProduct(int productId);
}
