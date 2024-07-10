<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, java.io.*"%>
<%@ page import="com.chainsys.ecomwebapplication.model.User"%>
<%@ page import="com.chainsys.ecomwebapplication.dao.CartDAO"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="com.chainsys.ecomwebapplication.model.WishlistItem"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Wishlist</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
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
            width: 400px; 
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

        .card .remove-from-wishlist {
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
            <button type="submit" class="btn btn-outline-secondary">Back to Products</button>
        </form>
    </div>
</nav>

<div class="container mt-5">
    <h1 class="mt-4">Your Wishlist</h1>
    <%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect("LoginForm.jsp");
        return;
    }

    WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	CartDAO cartDAO = (CartDAO) context.getBean("cartDAO");
	List<WishlistItem> wishlistItems = cartDAO.getWishlistItems(currentUser.getUserId());
    %>
    <% if (wishlistItems.isEmpty()) { %>
        <p>Your wishlist is empty.</p>
    <% } else { %>
        <div class="row">
            <% for (WishlistItem item : wishlistItems) { %>
                <div class="col-md-4 mb-3">
                    <div class="card">
                        <img src="data:image/jpeg;base64,<%= item.getProductImage() %>" class="card-img-top" alt="Product">
                        <div class="card-body">
                            <h5 class="card-title"><%= item.getProductName() %></h5>
                            <p class="card-text">Price: Rs.<%= item.getProductPrice() %></p>
                            <p class="card-text">Available Quantity: <%= item.getProductQuantity() %></p>
                            <form id="addToCartForm_<%= item.getProductId() %>" action="addToCart" method="POST">
                                <input type="hidden" name="productId" value="<%= item.getProductId() %>">
                                <button class="btn btn-success" type="submit">Add to Cart</button>
                            </form>
                            <button class="btn btn-danger remove-from-wishlist-btn" data-wishlist-id="<%= item.getWishlistId() %>">Remove from Wishlist</button>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"
        integrity="sha384-d1JcZm8dASb0mPv0RNFRpK0q8/6BJBmQKZNOs1/DYad66OpKefjUlCok2VdHbYD6"
        crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        let removeFromWishlistButtons = document.querySelectorAll('.remove-from-wishlist-btn');

        removeFromWishlistButtons.forEach(button => {
            button.addEventListener('click', function () {
                let wishlistId = button.getAttribute('data-wishlist-id');
                removeFromWishlist(wishlistId);
            });
        });
   
    });

    function removeFromWishlist(wishlistId) {
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, remove it!'
        }).then((result) => {
            if (result.isConfirmed) {
                let xhr = new XMLHttpRequest();
                xhr.open("POST", "removeFromWishlist", true);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        location.reload();
                    }
                };
                
                xhr.send("wishlistId=" + wishlistId);
            }
        });
    }
  
</script>
</body>
</html>
