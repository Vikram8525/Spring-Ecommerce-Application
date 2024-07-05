<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Base64, java.util.*, com.chainsys.ecomwebapplication.dao.ProductDAO, org.springframework.web.context.support.WebApplicationContextUtils, org.springframework.web.context.WebApplicationContext, java.text.SimpleDateFormat" %>
<%@ page import="com.chainsys.ecomwebapplication.model.ProductDetails"%>
<%@ page import="com.chainsys.ecomwebapplication.model.SellerDetails"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Product Details</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }
        .container {
            margin-top: 20px;
        }
        .carousel-inner img {
            max-width: 100%;
            height: 550px;
            object-fit: cover;
        }
        .best-seller {
            background-color: gold;
            color: black;
            padding: 5px;
            font-weight: bold;
            display: inline-block;
            margin-top: 10px;
        }
        .availability {
            font-size: 18px;
            margin-top: 10px;
        }
        .availability.in-stock {
            color: green;
        }
        .availability.limited-stock {
            color: yellow;
        }
        .availability.out-of-stock {
            color: red;
        }
        .button-group button {
            width: 600px;
            margin-right: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3" aria-label="Slide 4"></button>
                        <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="4" aria-label="Slide 5"></button>
                    </div>
                    <div class="carousel-inner">
                        <%
                            int productId = Integer.parseInt(request.getParameter("productId"));
                            int userId = 0;
                            List<String> imageBase64List = new ArrayList<>();
                            String productName = "";
                            String productDescription = "";
                            double productPrice = 0;
                            int productQuantity = 0;
                            String sellerName = "";
                            String sellerState = "";
                            String sellerCity = "";
                            boolean isBestSeller = false;

                            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
                            ProductDAO productDAO = (ProductDAO) context.getBean("productDAO");

                            try {
                            	ProductDetails productDetails = productDAO.getProductDetails(productId);
                            	SellerDetails sellerDetails = productDAO.getSellerDetails(productDetails.userId);
                                isBestSeller = productDAO.isBestSeller(productDetails.userId);

                                productName = productDetails.productName;
                                productDescription = productDetails.productDescription;
                                productPrice = productDetails.productPrice;
                                productQuantity = productDetails.productQuantity;
                                userId = productDetails.userId;
                                imageBase64List = productDetails.imageBase64List;

                                sellerName = sellerDetails.sellerName;
                                sellerState = sellerDetails.sellerState;
                                sellerCity = sellerDetails.sellerCity;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            for (int i = 0; i < imageBase64List.size(); i++) {
                                String base64Image = imageBase64List.get(i);
                        %>
                                <div class="carousel-item <%= i == 0 ? "active" : "" %>">
                                    <img src="data:image/jpeg;base64,<%= base64Image %>" class="d-block w-100" alt="Product <%= i + 1 %>">
                                </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <h2><%= productName %></h2>
                <p><%= productDescription %></p>
                <h3>Rs. <%= productPrice %></h3>
                <%
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, 7);
                    String deliveryDate = sdf.format(cal.getTime());
                %>
                <p>Delivered to your address by <%= deliveryDate %></p>
                <% if (isBestSeller) { %>
                    <div class="best-seller">Best Seller</div>
                <% } %>
                <p>Seller: <%= sellerName %></p>
                <p>State: <%= sellerState %></p>
                <p>City: <%= sellerCity %></p>
                <div class="availability <%= productQuantity > 10 ? "in-stock" : (productQuantity > 0 ? "limited-stock" : "out-of-stock") %>">
                    <%= productQuantity > 10 ? "Stock available in store" : (productQuantity > 0 ? "Final few stocks" : "Out of stock") %>
                </div>
                <div class="button-group">
                    <form action="CartServlet" method="POST" style="display:inline;">
                        <input type="hidden" name="productId" value="<%= productId %>">
                        <input type="hidden" name="action" value="addToCart">
                        <button type="submit" class="btn btn-success">Add to Cart</button>
                    </form>
                    <form action="AddToCart.jsp" method="POST" style="display:inline;">
                        <input type="hidden" name="productId" value="<%= productId %>">
                        <input type="hidden" name="action" value="wishlist">
                        <button type="submit" class="btn btn-primary">Add to Wishlist</i></button>
                    </form>
                    <form action="AddToCart.jsp" method="POST" style="display:inline;">
                        <input type="hidden" name="productId" value="<%= productId %>">
                        <input type="hidden" name="action" value="buyNow">
                        <button type="submit" class="btn btn-danger">Buy Now</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
