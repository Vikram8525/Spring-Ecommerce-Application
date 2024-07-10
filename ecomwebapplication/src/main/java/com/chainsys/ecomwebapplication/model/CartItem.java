package com.chainsys.ecomwebapplication.model;

public class CartItem {

	private int userId;
    private int productId;
    private int quantity;
    private double totalPrice;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	@Override
	public String toString() {
		return "CartItem [userId=" + userId + ", productId=" + productId + ", quantity=" + quantity + ", totalPrice="
				+ totalPrice + "]";
	}
	public CartItem(int userId, int productId, int quantity, double totalPrice) {
		
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
	public CartItem() {
		
	}
    
    
}
