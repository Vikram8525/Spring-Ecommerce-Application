<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, org.springframework.web.context.WebApplicationContext, org.springframework.web.context.support.WebApplicationContextUtils, com.chainsys.ecomwebapplication.dao.PaymentDAO"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 70px;
            font-family: 'Roboto', sans-serif;
            background-color: #f4f4f4;
        }

        .navbar {
            background-color: #232F3E;
            color: #ffffff;
        }

        .navbar .navbar-brand, .navbar .nav-link {
            color: #ffffff;
        }

        .form-control {
            background-color: #ffffff;
            color: #000;
        }

        .offcanvas-body {
            padding: 15px;
        }

        .btn-outline-success {
           background-color: #febd69;
            color: #232F3E;
        }

        .btn-outline-success:hover {
            background-color: #febd99;
            color: #232F3E;
        }

        .btn-outline-secondary {
            color: #ffffff;
            border-color: #ffffff;
        }

        .btn-outline-secondary:hover {
            background-color: #febd69;
            color: #232F3E;
        }

        .checkout-container {
            display: flex;
        }

        .checkout-details {
            flex: 1;
            padding: 20px;
        }

        .checkout-image {
            flex: 1;
            background-image: url('https://img.freepik.com/free-vector/mobile-messaging-modern-communication-technology-online-chatting-sms-texting-modern-leisure-activity-guy-checking-email-inbox-with-smartphone_335657-2.jpg?ga=GA1.1.1636780205.1718340265&semt=ais_user');
            background-size: cover;
            background-position: center;
        }

        .product-details {
            margin-bottom: 20px;
        }

        .btn-home {
            background-color: #232F3E;
            color: #ffffff;
            font-size: 20px;
            border-radius: 5px;
            padding: 8px 12px;
            border: none;
        }

        .btn-home:hover {
            background-color: #febd69;
            color: #232F3E;
        }

        .btn-payment {
            background-color: #febd69;
            color: #232F3E;
            border: none;
            padding: 10px 20px;
            font-size: 18px;
            border-radius: 5px;
        }

        .btn-payment:hover {
            background-color: #f0c14b;
            color: #232F3E;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark fixed-top">
    <div class="container-fluid">
        <form action="home.jsp" method="POST">
            <button class="btn-home" type="submit" name="home">
                <i class="fa fa-home"></i>
            </button>
        </form>
        
        <form action="ViewProduct.jsp" method="GET">
            <button type="submit" class="btn btn-outline-secondary">View All Products</button>
        </form>
    </div>
</nav>

<div class="checkout-container">
    <div class="checkout-details">
        <h1>Order Details</h1>
        <% 
            int userId = Integer.parseInt(request.getParameter("userId"));
            List<Map<String, Object>> cartItems = new ArrayList<>();
            double totalAmount = 0;
            
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            PaymentDAO checkoutService = (PaymentDAO) context.getBean("paymentDAO");

            cartItems = checkoutService.getCartItems(userId);
            for (Map<String, Object> item : cartItems) {
                totalAmount += (double) item.get("total_price");
            }
            
            Map<String, String> deliveryAddress = checkoutService.getDeliveryAddress(userId);
        %>
        
        <% if (!deliveryAddress.isEmpty()) { %>
            <h3>Delivery Address</h3>
            <p>State: <%= deliveryAddress.get("state") %></p>
            <p>City: <%= deliveryAddress.get("city") %></p>
            <p>Address: <%= deliveryAddress.get("address") %></p>
            <p>Pincode: <%= deliveryAddress.get("pincode") %></p>
            <form action="AddProfile.jsp" method="POST">
                <button type="submit" class="btn btn-outline-success">Add Address</button>
            </form>
        <% } %>
        
        <% if (!cartItems.isEmpty()) { %>
            <% int productIndex = 1; %>
            <% for (Map<String, Object> item : cartItems) { %>
                <div class="product-details">
                    <h4>Product <%= productIndex++ %></h4>
                    <p>Product Name: <%= item.get("product_name") %></p>
                    <p>Product Price: Rs.<%= item.get("product_price") %></p>
                    <p>Product Category: <%= item.get("product_category") %></p>
                    <p>Seller: <%= item.get("seller_name") %></p>
                    <p>Quantity in Cart: <%= item.get("quantity") %></p>
                    <p>Total Price of this Product: Rs.<%= item.get("total_price") %></p>
                </div>
            <% } %>
            
            <h3>Cart Total: Rs.<%= totalAmount %></h3>
            <form action="Payment.jsp" method="POST">
                <input type="hidden" name="userId" value="<%= userId %>">
                <button type="submit" class="btn btn-payment">Proceed to Payment</button>
            </form>
        <% } else { %>
            <p>Your cart is empty.</p>
        <% } %>
    </div>
    <div class="checkout-image"></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-oBqDVmMz4fnFO9gybBogGzA91ENi5gqbsaw3B56PonD5uvvM1hgS4pJp8ojL8p3h"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12xFf3AB7DoLP6FfZp+n91trG5tg1ANP1L5w8V4AXfW5yOFu"
        crossorigin="anonymous"></script>
</body>
</html>
