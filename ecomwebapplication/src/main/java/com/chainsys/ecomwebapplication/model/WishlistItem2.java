package com.chainsys.ecomwebapplication.model;

public class WishlistItem2 {

	private int userId;
    private int productId;
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
	@Override
	public String toString() {
		return "WishlistItem2 [userId=" + userId + ", productId=" + productId + "]";
	}
	public WishlistItem2(int userId, int productId) {
		this.userId = userId;
		this.productId = productId;
	}
	public WishlistItem2() {
		
	}
    
    
}
