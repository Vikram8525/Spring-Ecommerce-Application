package com.chainsys.ecomwebapplication.model;

public class WishlistItem {

	 private int wishlistId;
	    private int productId;
	    private String productName;
	    private String productImage;
	    private double productPrice;
	    private int productQuantity;
		public int getWishlistId() {
			return wishlistId;
		}
		public void setWishlistId(int wishlistId) {
			this.wishlistId = wishlistId;
		}
		public int getProductId() {
			return productId;
		}
		public void setProductId(int productId) {
			this.productId = productId;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getProductImage() {
			return productImage;
		}
		public void setProductImage(String productImage) {
			this.productImage = productImage;
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
		@Override
		public String toString() {
			return "WishlistItem [wishlistId=" + wishlistId + ", productId=" + productId + ", productName="
					+ productName + ", productImage=" + productImage + ", productPrice=" + productPrice
					+ ", productQuantity=" + productQuantity + "]";
		}
		public WishlistItem(int wishlistId, int productId, String productName, String productImage, double productPrice,
				int productQuantity) {
			this.wishlistId = wishlistId;
			this.productId = productId;
			this.productName = productName;
			this.productImage = productImage;
			this.productPrice = productPrice;
			this.productQuantity = productQuantity;
		}
		public WishlistItem() {
			
		}
	    
	    
}
