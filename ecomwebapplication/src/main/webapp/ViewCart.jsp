<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, java.io.*"%>
<%@ page import="com.chainsys.ecomwebapplication.model.User"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="com.chainsys.ecomwebapplication.dao.CartDAO"%>
<%@ page import="java.math.BigDecimal"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Cart</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<style>
body {
	padding-top: 70px; /* Ensure the navbar doesn't overlap the content */
	font-family: 'Roboto', sans-serif;
	background-color: #f4f4f4;
	padding: 20px;
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
	width: 400px; /* Adjusted width for the search bar */
}

.offcanvas-body {
	padding: 15px;
}

.btn-outline-success {
	color: #f1f1f1;
	border-color: #febd69;
}

.btn-outline-success:hover {
	background-color: #febd69;
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

.search-bar {
	width: 50%;
	margin: auto;
}

.card {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	max-width: 300px;
	margin: 20px;
	text-align: center;
	font-family: arial;
	float: left;
	cursor: pointer;
	transition: transform 0.2s;
	position: relative;
}

.card img {
	max-width: 100%;
	height: 300px;
	object-fit: cover;
}

.card:hover {
	transform: scale(1.05);
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.price {
	color: grey;
	font-size: 22px;
}

.card button {
	border: none;
	outline: 0;
	padding: 12px;
	color: white;
	text-align: center;
	cursor: pointer;
	width: 100%;
	font-size: 18px;
}

.card .remove-btn {
	background-color: #f44336; /* Red for remove button */
}

.card .update-btn {
	background-color: #28a745; /* Green for update button */
}

.card button:hover {
	opacity: 0.7;
}

.best-seller {
	position: absolute;
	top: 10px;
	left: 10px;
	background-color: gold;
	color: black;
	padding: 5px;
	font-weight: bold;
}

#noProductsFoundMessage {
	display: none;
	color: red;
	font-weight: bold;
	text-align: center;
	margin-top: 20px;
}

.nav-icons {
	display: flex;
	align-items: center;
	gap: 10px;
}

.nav-icons form {
	display: inline-block;
	margin: 0;
}

.nav-icons .btn {
	display: flex;
	align-items: center;
	justify-content: center;
	border: none;
	background-color: #232F3E;
	color: #ffffff;
	font-size: 20px;
	border-radius: 5px;
	padding: 8px 12px;
}

.nav-icons .btn:hover {
	background-color: #febd69;
	color: #232F3E;
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
				<button type="submit" class="btn btn-outline-secondary">View
					All Products</button>
			</form>
		</div>
	</nav>

	<div class="container mt-5">
		<h1 class="mt-4">Your Cart</h1>
		<%
		User currentUser = (User) session.getAttribute("user");
		if (currentUser == null) {
			String requestURI = request.getRequestURI() + "?" + request.getQueryString();
			session.setAttribute("redirectUrl", requestURI);
			response.sendRedirect("LoginForm.jsp");
			return;
		}

		int userId = currentUser.getUserId();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		CartDAO cartDAO = (CartDAO) context.getBean("cartDAO");
		List<Map<String, Object>> cartItems = cartDAO.getCartItemsForUser(userId);
		double totalAmount = cartItems.stream()
				.mapToDouble(item -> ((BigDecimal) item.get("product_price")).doubleValue() * (int) item.get("quantity")).sum();
		%>
		<%
		if (cartItems.isEmpty()) {
		%>
		<p>Your cart is empty.</p>
		<%
		} else {
		%>
		<div class="row">
			<%
			for (Map<String, Object> item : cartItems) {
			%>
			<div class="col-md-4 mb-3">
				<div class="card">
					<img src="data:image/jpeg;base64,<%=item.get("product_image")%>"
						class="card-img-top" alt="Product">
					<div class="card-body">
						<h5 class="card-title"><%=item.get("product_name")%></h5>
						<p class="card-text">
							Price: Rs.<%=item.get("product_price")%></p>
						<p class="card-text">
							Available Quantity:
							<%=item.get("product_quantity")%></p>
						<form action="updateCartItem" method="POST">
							<div class="input-group mb-3">
								<input type="number" class="form-control quantity-input"
									name="newQuantity" value="<%=item.get("quantity")%>" min="1">
								<input type="hidden" name="cartId"
									value="<%=item.get("cart_id")%>">
								<button class="btn btn-success update-btn" type="submit">Update
									Quantity</button>
							</div>
						</form>

						<input type="hidden" id="status"
							value="<%=request.getAttribute("status")%>"> <input
							type="hidden" id="message"
							value="<%=request.getAttribute("message")%>">
						<form action="deleteCartItem" method="POST">
							<input type="hidden" name="cartId"
								value="<%=item.get("cart_id")%>">
							<button class="btn btn-danger remove-btn" type="submit">Remove</button>
						</form>
					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
		<div class="mt-4">
			<h3>
				Total Amount: Rs.<%=totalAmount%></h3>
			<form action="OrderDetails.jsp" method="POST">
				<input type="hidden" name="userId" value="<%=userId%>">
				<button type="submit" class="btn btn-success">Proceed to
					Checkout</button>
			</form>
		</div>
		<%
		}
		%>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
		integrity="sha384-d1JcZm8dASb0mPv0RNFRpK0q8/6BJBmQKZNOs1/DYad66OpKefjUlCmUSaQ0Vf9l"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
		integrity="sha384-ZeZ2bBHIcJlVMpF8eyk3Lp5PaUlJ/J7UYfXGvHXyDKQoj4yTRb2e6A4wfn1QZGzY"
		crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script>
		var status = document.getElementById('status').value;
		var message = document.getElementById('message').value;

		if (status === "success") {
			Swal.fire({
				icon : 'success',
				title : 'Product removed from cart Successfully!',
				text : message
			});
		} else if (status === "failure") {
			Swal.fire({
				icon : 'error',
				title : 'Failed to remove Product!',
				text : message
			});
		}
	</script>
	<script>
		var status = document.getElementById('status').value;
		var message = document.getElementById('message').value;

		if (status === "succes") {
			Swal.fire({
				icon : 'succes',
				title : 'Product updated Successfully!',
				text : message
			});
		} else if (status === "fail") {
			Swal.fire({
				icon : 'error',
				title : 'Failed to update Product!',
				text : message
			});
		}
	</script>
</body>
</html>
