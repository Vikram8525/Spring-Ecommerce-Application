package com.chainsys.ecomwebapplication.model;

import java.util.Arrays;

public class WishlistProduct {
	
	 private String productName;
	    private byte[] productImage;
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public byte[] getProductImage() {
			return productImage;
		}
		public void setProductImage(byte[] productImage) {
			this.productImage = productImage;
		}
		@Override
		public String toString() {
			return "WishlistProduct [productName=" + productName + ", productImage=" + Arrays.toString(productImage)
					+ "]";
		}
		public WishlistProduct(String productName, byte[] productImage) {
			this.productName = productName;
			this.productImage = productImage;
		}
		public WishlistProduct() {
			
		}
	    
	    
}
