<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.chainsys.ecomwebapplication.model.User"%>
<%@ page import="com.chainsys.ecomwebapplication.dao.UserDAO"%>
<%@ page import="com.chainsys.ecomwebapplication.model.Product"%>
<%@ page import="com.chainsys.ecomwebapplication.dao.ProductDAO"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Base64" %>
<%@ page import="java.sql.Blob" %>

<%@ page import="com.chainsys.ecomwebapplication.model.Product"%>
	<%
	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	UserDAO userDAO = (UserDAO) context.getBean("userDAO");
	ProductDAO productDAO = (ProductDAO) context.getBean("productDAO");


    User user = (User) session.getAttribute("user");
    boolean isLoggedIn = (user != null);

    if (!isLoggedIn) {
        String requestURI = request.getRequestURI() + "?" + request.getQueryString();
        session.setAttribute("redirectUrl", requestURI);
        response.sendRedirect("LoginForm.jsp");
        return;
    }

    int cartItemCount = userDAO.getCartItemCount(user);
    int wishlistItemCount = userDAO.getWishlistItemCount(user);
%>


<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View Products</title>
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
	padding-top: 70px; 
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

.card .add-to-cart {
	background-color: #000;
}

.card .add-to-wishlist {
	background-color: #f44336;
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
  <script>
        function redirectToProductDetails(productId) {
            window.location.href = 'ProductDetails.jsp?productId=' + productId;
        }

        document.addEventListener('DOMContentLoaded', function () {
            const searchInput = document.getElementById('searchInput');
            const noProductsFoundMessage = document.getElementById('noProductsFoundMessage');

            searchInput.addEventListener('input', function () {
                const searchValue = searchInput.value.toLowerCase();
                const cards = document.querySelectorAll('.card');
                let visibleCardCount = 0;

                cards.forEach(card => {
                    const productName = card.querySelector('h3').textContent.toLowerCase();
                    if (productName.includes(searchValue)) {
                        card.style.display = '';
                        visibleCardCount++;
                    } else {
                        card.style.display = 'none';
                    }
                });

                if (visibleCardCount === 0) {
                    noProductsFoundMessage.style.display = 'block';
                } else {
                    noProductsFoundMessage.style.display = 'none';
                }
            });
        });

        function updateCartNotification(count) {
            const cartButton = document.querySelector('.fa-shopping-cart');
            if (count > 0) {
                const notification = document.createElement('span');
                notification.className = 'cart-notification';
                notification.textContent = count;
                cartButton.parentElement.appendChild(notification);
            }
        }
		
        function updateWishlistNotification(count) {
            const wishlistButton = document.querySelector('.fa-heart-o');
            if (count > 0) {
                const notification = document.createElement('span');
                notification.className = 'wishlist-notification';
                notification.textContent = count;
                wishlistButton.parentElement.appendChild(notification);
            }
        }
        
        document.addEventListener('DOMContentLoaded', function () {
            const cartItemCount = <%= cartItemCount %>;
            updateCartNotification(cartItemCount);
            
            const wishlistItemCount = <%= wishlistItemCount %>;
            updateWishlistNotification(wishlistItemCount);
        });
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
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarToggler"
				aria-controls="navbarToggler" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarToggler">
				<form class="d-flex mx-auto" role="search">
					<input id="searchInput" class="form-control me-2" type="search"
						placeholder="Search" aria-label="Search">
				</form>
				<div class="nav-icons">
					<form action="ViewWishlist.jsp">
						<button class="btn btn-secondary" type="submit" name="wishlist">
							<i class="fa fa-heart-o"></i>
						</button>
					</form>
					<form action="ViewCart.jsp" method="POST" class="box">
						<button class="btn btn-secondary" type="submit" name="cart">
							<i class="fa fa-shopping-cart"></i>
						</button>
					</form>
					<form action="ViewProduct.jsp" method="GET">
						<button class="btn btn-secondary" type="submit" name="showAll">Show
							All</button>
					</form>
					<button class="btn btn-outline-secondary" type="button"
						data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar"
						aria-controls="offcanvasNavbar">Filter</button>
				</div>
			</div>
		</div>
	</nav>

	<div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar"
		aria-labelledby="offcanvasNavbarLabel">
		<div class="offcanvas-header">
			<h5 class="offcanvas-title" id="offcanvasNavbarLabel">Filter
				Options</h5>
			<button type="button" class="btn-close" data-bs-dismiss="offcanvas"
				aria-label="Close"></button>
		</div>
		<div class="offcanvas-body">
			<form method="GET" action="ViewProduct.jsp">
				<div class="mb-3">
					<label for="productType" class="form-label">Product Type</label> <select
						class="form-select" id="productType" name="productType">
						<option value="">Choose...</option>
						<option value="watch">Watch</option>
						<option value="shoes">Shoes</option>
						<option value="anime_action_figures">Anime Action Figures</option>
						<option value="mobile_phone">Mobile Phone</option>
						<option value="headset">Headset</option>
					</select>
				</div>
				<div class="mb-3">
					<div class="d-flex align-items-center">
						<label for="minPrice" class="form-label me-2">Min Price</label> <select
							class="form-select" id="minPrice" name="minPrice">
							<option value="">Choose...</option>
							<option value="50">50</option>
							<option value="100">100</option>
							<option value="200">200</option>
							<option value="500">500</option>
							<option value="1000">1000</option>
							<option value="2000">2000</option>
							<option value="5000">5000</option>
							<option value="10000">10000</option>
							<option value="20000">20000</option>
						</select>
					</div>
				</div>
				<div class="mb-3">
					<div class="d-flex align-items-center">
						<label for="maxPrice" class="form-label me-2">Max Price</label> <select
							class="form-select" id="maxPrice" name="maxPrice">
							<option value="">Choose...</option>
							<option value="50">50</option>
							<option value="100">100</option>
							<option value="200">200</option>
							<option value="500">500</option>
							<option value="1000">1000</option>
							<option value="2000">2000</option>
							<option value="5000">5000</option>
							<option value="10000">10000</option>
							<option value="20000">20000</option>
						</select>
					</div>
				</div>
				<button type="submit" class="btn btn-primary">Apply Filters</button>
			</form>
		</div>
	</div>

	<h2>Products</h2>
	<div id="noProductsFoundMessage">No products found.</div>

	<%
        String productType = request.getParameter("productType");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        String searchValue = request.getParameter("searchValue");

        double minPrice = minPriceStr != null && !minPriceStr.isEmpty() ? Double.parseDouble(minPriceStr) : 0;
        double maxPrice = maxPriceStr != null && !maxPriceStr.isEmpty() ? Double.parseDouble(maxPriceStr) : Double.MAX_VALUE;

        Map<Integer, String> sellerMap = productDAO.getAllSellers();
        Map<Integer, Integer> sellerItemsSoldMap = productDAO.getSellerItemsSold();
        int bestSellerId = productDAO.getBestSellerId(sellerItemsSoldMap);
        List<Product> products = productDAO.getFilteredProducts(productType, minPrice, maxPrice, searchValue);

        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                int productId = product.getProductId();
                String productName = product.getProductName();
                double productPrice = product.getProductPrice();
                byte[] productImage = product.getProductImage();
                String base64Image = "";
                if (productImage != null) {
                    base64Image = Base64.getEncoder().encodeToString(productImage);
                }
                String sellerName = sellerMap.get(product.getUserId());
                boolean isBestSeller = product.getUserId() == bestSellerId;
        %>
	
	<div class="card" onclick="redirectToProductDetails(<%=productId%>)">
		<%
		if (isBestSeller) {
		%>
		<div class="best-seller">Best Seller</div>
		<%
		}
		%>
		<img src="data:image/jpeg;base64,<%=base64Image%>"
			alt="Product ">
		<h3><%=productName%></h3>
		<p class="price">
			Rs.<%=productPrice%></p>
		<p>
			Seller:
			<%=sellerName%></p>
		<p><form action="addToCart" method="POST">
			<input type="hidden" name="productId" value="<%=productId%>">
    <button class="add-to-cart" type="submit">Add to Cart</button>
</form>

                    </p>
		
                    <p>
                        
		<form action="addToWishlist" method="POST">
                            <input type="hidden" name="productId"
				value="<%=productId%>">
                            <button class="add-to-wishlist"
				type="submit">Add to Wishlist</button>
                        </form>
                    </p>
                </div>
    <%
    }
    } else {
    %>
            <div id="noProductsFoundMessage">No products found.</div>
    <%
    }
    %>

    <script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
		integrity="sha384-d1JcZm8dASb0mPv0RNFRpK0q8/6BJBmQKZNOs1/DYad66OpKefjUlCok2VdHbYD6"
		crossorigin="anonymous"></script>
    <script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>

</html>
