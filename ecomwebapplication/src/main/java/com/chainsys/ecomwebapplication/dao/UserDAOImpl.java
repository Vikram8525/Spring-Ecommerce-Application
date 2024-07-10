package com.chainsys.ecomwebapplication.dao;


import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chainsys.ecomwebapplication.mapper.UserMapper;
import com.chainsys.ecomwebapplication.model.User;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Random RANDOM = new Random();
    
    @Override
    public boolean addUser(User user) {
        String insertUserQuery = "INSERT INTO users (user_id, user_name, user_email, password) VALUES (?, ?, ?, ?)";
        String updateWalletQuery = "UPDATE users SET wallet_balance = 100000 WHERE user_id = ?";

        int rowsInserted = jdbcTemplate.update(insertUserQuery, user.getUserId(), user.getUserName(),
                user.getUserEmail(), user.getPassword());

        if (rowsInserted > 0) {
            int rowsUpdated = jdbcTemplate.update(updateWalletQuery, user.getUserId());
            return rowsUpdated > 0;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, address=?, state=?, city=?, pincode=? WHERE user_id=?";
        try {
            int rowsUpdated = jdbcTemplate.update(sql,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAddress(),
                    user.getState(),
                    user.getCity(),
                    user.getPincode(),
                    user.getUserId());
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int generateUniqueUserId() {
        String query = "SELECT user_id FROM users WHERE user_id = ?";
        int userId;
        boolean unique;
        do {
            userId = 1000 + RANDOM.nextInt(9000);
            unique = jdbcTemplate.query(query, (rs) -> !rs.next(), userId);
        } while (!unique);
        return userId;
    }

    @Override
    public boolean sendWelcomeEmail(User user) {
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
            if (user.getUserName() == null || user.getUserName().isEmpty() || user.getUserEmail() == null
                    || user.getUserEmail().isEmpty()) {
                User dbUser = getUserDetailsFromDatabase(user.getUserId());
                if (dbUser == null) {
                    throw new IllegalArgumentException("User details not found in the database");
                }
                if (user.getUserName() == null || user.getUserName().isEmpty()) {
                    user.setUserName(dbUser.getUserName());
                }
                if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
                    user.setUserEmail(dbUser.getUserEmail());
                }
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getUserEmail()));
            message.setSubject("Welcome to our E-commerce Application");

            
            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Dear " + user.getUserName() + ",<br><br>");
            emailBody.append("Welcome to our E-commerce Application!<br><br>");
            emailBody.append("Your user ID is: <b>" + user.getUserId() + "</b><br><br>");
            emailBody.append("Please remember your user ID for future reference.<br><br>");
            emailBody.append("Thank you for joining us!");

            message.setContent(emailBody.toString(), "text/html");

            Transport.send(message);

            return true;
        } catch (MessagingException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserDetailsFromDatabase(int userId) {
        String query = "SELECT user_name, user_email FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(query, new UserMapper(), userId);
    }

    @Override
    public User getUserByIdAndPassword(int userId, String password) {
        try {
            String query = "SELECT user_id, user_name, user_email, password, created_at, updated_at, is_seller, is_deleted, num_items_bought, num_items_sold, wallet_balance, first_name, last_name, address, state, city, pincode FROM users WHERE user_id = ? AND password = ?";
            return jdbcTemplate.queryForObject(query, new UserMapper(), userId, password);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RuntimeException("Error accessing database", e);
        }
    }

    @Override
    public boolean isUserExists(String userEmail, int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE user_email = ? OR user_id = ?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, userEmail, userId);
        return count > 0;
    }
    
    @Override
    public int getCartItemCount(User user) {
        if (user == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) AS cartItemCount FROM Cart WHERE user_id = ? AND is_bought = 0 ";
        return jdbcTemplate.queryForObject(sql, Integer.class, user.getUserId());
    }

    @Override
    public int getWishlistItemCount(User user) {
        if (user == null) {
            return 0;
        }
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, user.getUserId());
    }
    

}
