package com.chainsys.ecomwebapplication.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Repository("cartBuyNowDAO")
public class CartBuyNowDAOImpl implements CartBuyNowDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String PRODUCT_ATTRIBUTE = "product_name";
    private static final String QUANTITY_ATTRIBUTE = "quantity";
    private static final String SUCCESS_ATTRIBUTE = "Success";
    private static final String PRODID_ATTRIBUTE = "product_id";

    @Override
    public String checkWalletBalance(int userId) {
        String query = "SELECT SUM(p.product_price * c.quantity) AS total_amount, u.wallet_balance " +
                "FROM Cart c JOIN Products p ON c.product_id = p.product_id " +
                "JOIN Users u ON c.user_id = u.user_id " +
                "WHERE c.user_id = ? AND c.is_bought = 0";

        try {
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
                double totalAmount = rs.getDouble("total_amount");
                double walletBalance = rs.getDouble("wallet_balance");
                if (walletBalance < totalAmount) {
                    return "Insufficient wallet balance.Add balance in your profile";
                }
                return SUCCESS_ATTRIBUTE;
            }, new Object[]{userId});
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to check wallet balance.";
        }
    }

    @Override
    public String updateProductQuantities(int userId) {
        String selectQuery = "SELECT c.product_id, c.quantity, p.product_quantity, p.user_id AS seller_id " +
                "FROM Cart c JOIN Products p ON c.product_id = p.product_id " +
                "WHERE c.user_id = ? AND c.is_bought = 0";

        try {
            jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                int productId = rs.getInt(PRODID_ATTRIBUTE);
                int quantity = rs.getInt(QUANTITY_ATTRIBUTE);
                int sellerId = rs.getInt("seller_id");

                jdbcTemplate.update("UPDATE Products SET product_quantity = product_quantity - ? WHERE product_id = ?", quantity, productId);
                jdbcTemplate.update("UPDATE Users SET num_items_sold = num_items_sold + ? WHERE user_id = ?", quantity, sellerId);

                return null;
            }, new Object[]{userId});

            int totalQuantity = jdbcTemplate.queryForObject(
                    "SELECT SUM(quantity) FROM Cart WHERE user_id = ? AND is_bought = 0",
                     Integer.class ,new Object[]{userId});

            jdbcTemplate.update("UPDATE Users SET num_items_bought = num_items_bought + ? WHERE user_id = ?", totalQuantity, userId);

            return SUCCESS_ATTRIBUTE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update product quantities.";
        }
    }

    @Override
    public String fillOrderDetails(int userId, String orderNumber) {
        String selectQuery = "SELECT c.product_id, c.quantity, p.product_name, p.product_image, p.product_price " +
                "FROM Cart c JOIN Products p ON c.product_id = p.product_id " +
                "WHERE c.user_id = ? AND c.is_bought = 0";

        String insertQuery = "INSERT INTO order_details (user_id, product_id, order_number, product_name, product_image, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                int productId = rs.getInt(PRODID_ATTRIBUTE);
                int quantity = rs.getInt(QUANTITY_ATTRIBUTE);
                String productName = rs.getString(PRODUCT_ATTRIBUTE);
                byte[] productImage = rs.getBytes("product_image");
                double productPrice = rs.getDouble("product_price");
                double totalPrice = quantity * productPrice;

                jdbcTemplate.update(insertQuery, userId, productId, orderNumber, productName, productImage, quantity, totalPrice);
                return null;
            }, new Object[]{userId});
            return SUCCESS_ATTRIBUTE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fill order details.";
        }
    }

    @Override
    public String fillPaymentDetails(int userId, String orderNumber, String paymentMethod) {
        String selectQuery = "SELECT order_id, total_price FROM order_details WHERE user_id = ? AND order_number = ?";
        String insertQuery = "INSERT INTO payments (order_id, user_id, payment_method, payment_status, amount) VALUES (?, ?, ?, 'Completed', ?)";

        try {
            jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                int orderId = rs.getInt("order_id");
                double totalPrice = rs.getDouble("total_price");

                jdbcTemplate.update(insertQuery, orderId, userId, paymentMethod, totalPrice);
                return null;
            }, new Object[]{userId, orderNumber});
            return SUCCESS_ATTRIBUTE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fill payment details.";
        }
    }

    
    @Override
    public String updateWalletBalances(int userId, String orderNumber) {
        String query = "SELECT od.product_id, od.total_price, p.user_id AS seller_id " +
                "FROM order_details od JOIN Products p ON od.product_id = p.product_id " +
                "WHERE od.user_id = ? AND od.order_number = ?";

        try {
            jdbcTemplate.query(query, (rs, rowNum) -> {
                double totalPrice = rs.getDouble("total_price");
                int sellerId = rs.getInt("seller_id");

                jdbcTemplate.update("UPDATE Users SET wallet_balance = wallet_balance - ? WHERE user_id = ?", totalPrice, userId);
                jdbcTemplate.update("UPDATE Users SET wallet_balance = wallet_balance + ? WHERE user_id = ?", totalPrice, sellerId);
                return null;
            }, new Object[]{userId, orderNumber});
            return SUCCESS_ATTRIBUTE;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update wallet balances.";
        }
    }

    @Override
    public String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    @Override
    public String markItemsAsBought(int userId) {
        String updateQuery = "UPDATE Cart SET is_bought = 1 WHERE user_id = ? AND is_bought = 0";

        try {
            int rowsAffected = jdbcTemplate.update(updateQuery, userId);
            if (rowsAffected > 0) {
                return SUCCESS_ATTRIBUTE;
            } else {
                return "No items found in the cart to mark as bought.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update cart items.";
        }
    }

    private String calculateDeliveryDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date deliveryDate = cal.getTime();
        return deliveryDate.toString();
    }

    @Override
    public boolean sendOrderEmails(int userId, String orderNumber) {
        final String username = "tarzan.shopping.in@gmail.com";
        final String password = "zvhs waup gshd pert";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Get buyer details
            String buyerQuery = "SELECT user_name, user_email, address, city, state, pincode FROM Users WHERE user_id = ?";
            String[] buyerDetails = jdbcTemplate.queryForObject(buyerQuery, (rs, rowNum) -> {
                return new String[]{
                        rs.getString("user_name"),
                        rs.getString("user_email"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("pincode")
                };
            }, new Object[]{userId});

            String buyerName = buyerDetails[0];
            String buyerEmail = buyerDetails[1];
            String buyerAddress = buyerDetails[2];
            String buyerCity = buyerDetails[3];
            String buyerState = buyerDetails[4];
            String buyerPincode = buyerDetails[5];

            // Get order and seller details
            String orderQuery = "SELECT od.product_name, od.quantity, p.user_id AS seller_id, u.user_name AS seller_name, u.user_email AS seller_email, p.product_id " +
                    "FROM order_details od " +
                    "JOIN Products p ON od.product_id = p.product_id " +
                    "JOIN Users u ON p.user_id = u.user_id " +
                    "WHERE od.user_id = ? AND od.order_number = ?";

            String deliveryDate = calculateDeliveryDate();

            // Send email to buyer
            StringBuilder buyerEmailBody = new StringBuilder();
            buyerEmailBody.append("Dear ").append(buyerName).append(",<br><br>");
            buyerEmailBody.append("Thank you for shopping with us.<br><br>");
            buyerEmailBody.append("Your order with the following details will be received on or before ")
                    .append(deliveryDate).append(":<br><br>");

            List<Map<String, Object>> orderDetails = jdbcTemplate.queryForList(orderQuery, userId, orderNumber);

            for (Map<String, Object> orderDetail : orderDetails) {
                String productName = (String) orderDetail.get("product_name");
                int quantity = (int) orderDetail.get("quantity");
                int productId = (int) orderDetail.get("product_id");
                String sellerName = (String) orderDetail.get("seller_name");
                String sellerEmail = (String) orderDetail.get("seller_email");

                buyerEmailBody.append(productName).append(" - Quantity: ").append(quantity).append("<br>");

                // Send email to seller
                Message sellerMessage = new MimeMessage(session);
                sellerMessage.setFrom(new InternetAddress(username));
                sellerMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sellerEmail));
                sellerMessage.setSubject("New Order Notification");

                StringBuilder sellerEmailBody = new StringBuilder();
                sellerEmailBody.append("Dear ").append(sellerName).append(",<br><br>");
                sellerEmailBody.append("The user with ID ").append(userId).append(" (").append(buyerName)
                        .append(") has ordered your product ID ")
                        .append(productId).append(" (").append(productName).append(") for the quantity of ")
                        .append(quantity).append(".<br>");
                sellerEmailBody.append("Order number: ").append(orderNumber).append("<br>");
                sellerEmailBody.append("The customer address is: ").append(buyerAddress).append(", ")
                        .append(buyerCity).append(", ").append(buyerState).append(", ").append(buyerPincode).append("<br>");
                sellerEmailBody.append("Please deliver the product from your store to the customer as soon as possible.<br><br>");
                sellerEmailBody.append("Thank you!");

                sellerMessage.setContent(sellerEmailBody.toString(), "text/html");
                Transport.send(sellerMessage);
            }

            buyerEmailBody.append("<br>Your order ID is ").append(orderNumber).append(".<br><br>");
            buyerEmailBody.append("Your order will be delivered on or before ").append(deliveryDate).append(".<br><br>");
            buyerEmailBody.append("Please ensure someone is available to receive the package.<br><br>");
            buyerEmailBody.append("Thank you!");

            Message buyerMessage = new MimeMessage(session);
            buyerMessage.setFrom(new InternetAddress(username));
            buyerMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(buyerEmail));
            buyerMessage.setSubject("Order Confirmation");
            buyerMessage.setContent(buyerEmailBody.toString(), "text/html");

            Transport.send(buyerMessage);

            return true;
        } catch (MessagingException | DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
