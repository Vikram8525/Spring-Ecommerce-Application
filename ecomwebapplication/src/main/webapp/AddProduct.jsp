<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.chainsys.ecomwebapplication.model.User"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <title>Add Product</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 80px auto; /* Adjusted margin for better alignment */
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input, .form-group textarea, .form-group select {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
        }
        .form-group input[type="file"] {
            padding: 3px;
        }
        .form-group textarea {
            resize: vertical;
        }
        .form-group button {
            padding: 10px 20px;
            background: #5cb85c;
            color: #fff;
            border: none;
            cursor: pointer;
        }
        .form-group button:hover {
            background: #4cae4c;
        }
        
        .navbar {
    background-color: #232F3E;
    position: fixed; /* Fixed position to stick to the top */
    width: 100%; /* Adjusted width to create some space on both sides */
    top: 0;
    left: 0;
   /* Adding left margin */
    z-index: 1000; /* Ensure it's above other content */
}

.navbar .container-fluid {
    display: flex;
    justify-content: space-between;
    padding: 10px 20px; /* Added padding for better spacing */
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

        .btn.btn-outline-secondary {
            color: #ffffff;
            border-color: #ffffff;
            margin-left: 10px;
        }

        .btn.btn-outline-secondary:hover {
            background-color: #febd69;
            color: #232F3E;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark fixed-top">
    <div class="container-fluid">
        <form action="SellerViewProducts.jsp" method="POST">
            <button class="btn-home" type="submit" name="home">
                <i class="fa fa-arrow-left"></i>
            </button>
        </form>
    </div>
</nav>

<div class="container">
    <h1>Add Product</h1>
    <form action="addProduct" method="post" enctype="multipart/form-data">
        
        <div class="form-group">
            <label for="user_id">User ID</label>
            <input type="number" id="user_id" name="user_id" value="<%= request.getSession().getAttribute("user") != null ? ((User) request.getSession().getAttribute("user")).getUserId() : "" %>" readonly>
        </div>
        <div class="form-group">
            <label for="seller_name">Seller Name</label>
            <input type="text" id="seller_name" name="seller_name" value="<%= request.getSession().getAttribute("user") != null ? ((User) request.getSession().getAttribute("user")).getUserName() : "" %>" readonly>
        </div>
        <div class="form-group">
            <label for="category_name">Category Name</label>
            <select id="category_name" name="category_name" required>
                <option value="watch">Watch</option>
                <option value="shoes">Shoes</option>
                <option value="anime_action_figures">Anime Action Figures</option>
                <option value="mobile_phone">Mobile Phone</option>
                <option value="headset">Headset</option>
            </select>
        </div>
        <div class="form-group">
            <label for="product_name">Product Name</label>
            <input type="text" id="product_name" name="product_name" required>
        </div>
        <div class="form-group">
            <label for="product_image">Product Image</label>
            <input type="file" id="product_image" name="product_image" required>
        </div>
        <div class="form-group">
            <label for="sample_image">Sample Image</label>
            <input type="file" id="sample_image" name="sample_image" required>
        </div>
        <div class="form-group">
            <label for="left_image">Left Image</label>
            <input type="file" id="left_image" name="left_image" required>
        </div>
        <div class="form-group">
            <label for="right_image">Right Image</label>
            <input type="file" id="right_image" name="right_image" required>
        </div>
        <div class="form-group">
            <label for="bottom_image">Bottom Image</label>
            <input type="file" id="bottom_image" name="bottom_image" required>
        </div>
        <div class="form-group">
            <label for="product_price">Product Price</label>
            <input type="number" step="0.01" id="product_price" name="product_price" required>
        </div>
        <div class="form-group">
            <label for="product_description">Product Description</label>
            <textarea id="product_description" name="product_description" required></textarea>
        </div>
        <div class="form-group">
            <label for="product_quantity">Product Quantity</label>
            <input type="number" id="product_quantity" name="product_quantity" required>
        </div>
        <div class="form-group">
            <button type="submit">Add Product</button>
        </div>
    </form>
</div>

</body>
</html>
