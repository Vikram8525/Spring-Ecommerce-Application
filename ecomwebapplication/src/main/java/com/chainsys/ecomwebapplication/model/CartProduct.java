package com.chainsys.ecomwebapplication.model;

import java.util.Arrays;

public class CartProduct {

	 private String productName;
	    private byte[] productImage;
	    private double productPrice;
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
		public double getProductPrice() {
			return productPrice;
		}
		public void setProductPrice(double productPrice) {
			this.productPrice = productPrice;
		}
		@Override
		public String toString() {
			return "CartProduct [productName=" + productName + ", productImage=" + Arrays.toString(productImage)
					+ ", productPrice=" + productPrice + "]";
		}
		public CartProduct(String productName, byte[] productImage, double productPrice) {
			
			this.productName = productName;
			this.productImage = productImage;
			this.productPrice = productPrice;
		}
		public CartProduct() {
			
		}
	    
	    
}
