<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, org.springframework.web.context.WebApplicationContext, org.springframework.web.context.support.WebApplicationContextUtils, com.chainsys.ecomwebapplication.dao.PaymentDAO"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 70px;
            font-family: 'Arial', sans-serif;
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
            background-image: url('background.jpg');
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
        .btn-buy {
            background-color: #febd69;
            color: #232F3E;
            border: none;
            padding: 10px 20px;
            font-size: 18px;
            border-radius: 5px;
        }
        .btn-buy:hover {
            background-color: #f0c14b;
            color: #232F3E;
        }
        .payment-method {
            font-size: 24px; /* Larger font size */
            color: #800080; /* Purple color */
            font-weight: bold; /* Bold text */
            margin-top: 20px;
        }
        .change-payment-btn {
            margin-top: 10px;
        }
    </style>
    <script type="text/javascript">
        function validateForm() {
            var userId = document.forms["orderForm"]["userId"].value;
            if (userId == null || userId == "") {
                alert("User ID is missing.");
                return false;
            }
            return true;
        }
    </script>
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
            String paymentMethod = request.getParameter("paymentMethod");
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
            %>
            <h1 class="payment-method">
                Payment Method: <%=paymentMethod%>
            </h1>
            <%
            }
            %>
            <%
            String userIdStr = request.getParameter("userId");
            if (userIdStr == null || userIdStr.isEmpty()) {
                out.println("<p style='color: red;'>User ID is missing.</p>");
                return;
            }

            int userId;
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                out.println("<p style='color: red;'>Invalid User ID.</p>");
                return;
            }

            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            PaymentDAO paymentDAO = (PaymentDAO) context.getBean("paymentDAO");

            List<Map<String, Object>> cartItems = paymentDAO.getCartItems(userId);
            Map<String, String> deliveryAddress = paymentDAO.getDeliveryAddress(userId);

            double totalAmount = 0;
            for (Map<String, Object> item : cartItems) {
                double totalPrice = (Double) item.get("total_price");
                totalAmount += totalPrice;
            }
            %>

            <h3>Delivery Address</h3>
            <p>State: <%=deliveryAddress.get("state")%></p>
            <p>City: <%=deliveryAddress.get("city")%></p>
            <p>Address: <%=deliveryAddress.get("address")%></p>
            <p>Pincode: <%=deliveryAddress.get("pincode")%></p>

            <%
            if (!cartItems.isEmpty()) {
                int productIndex = 1;
                for (Map<String, Object> item : cartItems) {
            %>
            <div class="product-details">
                <h4>Product <%=productIndex++%></h4>
                <p>Product Name: <%=item.get("product_name")%></p>
                <p>Product Price: Rs.<%=item.get("product_price")%></p>
                <p>Product Category: <%=item.get("product_category")%></p>
                <p>Seller: <%=item.get("seller_name")%></p>
                <p>Quantity in Cart: <%=item.get("quantity")%></p>
                <p>Total Price of this Product: Rs.<%=item.get("total_price")%></p>
            </div>
            <%
                }
            %>
            <h3>Cart Total: Rs.<%=totalAmount%></h3>

            <form action="Payment.jsp" method="GET" class="change-payment-btn">
                <input type="hidden" name="userId" value="<%=userId%>">
                <button type="submit" class="btn btn-outline-success">Change Payment Method</button>
            </form>
			<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
			<input type="hidden" id="message" value="<%= request.getAttribute("message") %>">
			
            <form name="orderForm" action="cartBuyNow" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="userId" value="<%=userId%>">
                <input type="hidden" name="paymentMethod" value="<%=paymentMethod%>">
                <button type="submit" class="btn-buy">Buy Now</button>
            </form>
            <%
            } else {
            %>
            <p>Your cart is empty.</p>
            <%
            }
            %>
        </div>
        <div class="checkout-image"></div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
        var status = document.getElementById('status').value;
    var message = document.getElementById('message').value;

            if (status === "success") {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: message
                });
            } else if (status === "failure") {
                Swal.fire({
                    icon: 'error',
                    title: 'error!',
                    text: message
                });
            }
        
    </script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-d1JcZm8dASb0mPv0RNFRpK0q8/6BJBmQKZNOs1/DYad66OpKefjUlCok2VdHbYD6" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXcG7FMGRaIDYmdvZ5C72fNf6lW81Qi0ZMDrIUIvd+9A8" crossorigin="anonymous"></script>
</body>
</html>
