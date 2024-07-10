package com.chainsys.ecomwebapplication.model;

import java.util.List;

public class ProductDetails {

	public String productName;
    public String productDescription;
    public double productPrice;
    public int productQuantity;
    public int userId;
    public List<String> imageBase64List;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<String> getImageBase64List() {
		return imageBase64List;
	}
	public void setImageBase64List(List<String> imageBase64List) {
		this.imageBase64List = imageBase64List;
	}
    
    
}
