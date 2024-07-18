<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.Random" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Form</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .btn {
            display: inline-block;
            margin: 10px;
            padding: 15px 30px;
            font-size: 16px;
            font-weight: 500;
            color: #fff;
            background-color: #007bff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            position: relative;
            transition: background-color 0.3s, transform 0.3s;
        }

        .btn:hover {
            background-color: #0056b3;
            transform: translateY(-5px);
        }

        .notification {
            display: none;
            position: absolute;
            bottom: -40px;
            left: 50%;
            transform: translateX(-50%);
            background-color: #333;
            color: #fff;
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 14px;
            white-space: nowrap;
            z-index: 1;
            opacity: 0;
            transition: opacity 0.3s;
        }

        .btn:hover .notification {
            display: block;
            opacity: 1;
        }

        .error {
            color: red;
            font-size: 12px;
            margin-top: 4px;
        }
    </style>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
</head>
<body>
    <div class="container mt-5">
        <h2>Register</h2>
        <form action="registration" method="POST" id="registrationForm" onsubmit="return validateForm()">
            <div class="mb-3">
                <label for="user_id" class="form-label">User ID</label>
                <div class="input-group">
                    <input type="text" class="form-control" id="user_id" name="user_id" readonly required>
                    <button type="button" class="btn btn-outline-secondary" onclick="generateUserId()">
                        <i class="fa fa-refresh"></i>
                    </button>
                </div>
            </div>
            <div class="mb-3">
                <label for="user_name" class="form-label">User Name</label>
                <input type="text" class="form-control" id="user_name" name="user_name" required pattern="[a-zA-Z_]{3,20}">
                <div id="user_name_error" class="error"></div>
            </div>
            <div class="mb-3">
                <label for="user_email" class="form-label">Email</label>
                <input type="email" class="form-control" id="user_email" name="user_email" required pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$">
                <div id="user_email_error" class="error"></div>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required pattern="(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}">
                <div id="password_error" class="error"></div>
            </div>
            <div class="mb-3">
                <label for="confirm_password" class="form-label">Confirm Password</label>
                <input type="password" class="form-control" id="confirm_password" required pattern="(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}">
                <div id="confirm_password_error" class="error"></div>
            </div>
            <button type="submit" class="btn btn-primary">Register</button>
        </form>
        <!-- Login Button -->
        <input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
        <input type="hidden" id="message" value="<%= request.getAttribute("message") %>">
    
        <form class="form" action="login">
            <button class="btn" type="submit" id="loginBtn">Login
                <div class="notification">If you are an existing user, choose me</div>
            </button>
        </form>
    </div>

    <script>
    function generateUserId() {
        fetch('/generateUserId')
            .then(response => response.json())
            .then(data => {
                document.getElementById('user_id').value = data.userId;
            })
            .catch(error => console.error('Error:', error));
    }

    function validateForm() {
        var isValid = true;

        var userNameInput = document.getElementById('user_name');
        var userNameError = document.getElementById('user_name_error');
        if (!/^[a-zA-Z_]{3,20}$/.test(userNameInput.value)) {
            userNameError.textContent = 'Please enter a valid user name (3-20 characters, letters only).';
            isValid = false;
        } else {
            userNameError.textContent = '';
        }

        var emailInput = document.getElementById('user_email');
        var emailError = document.getElementById('user_email_error');
        if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(emailInput.value)) {
            emailError.textContent = 'Please enter a valid email address.';
            isValid = false;
        } else {
            emailError.textContent = '';
        }

        var passwordInput = document.getElementById('password');
        var passwordError = document.getElementById('password_error');
        if (!/(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}/.test(passwordInput.value)) {
            passwordError.textContent = 'Password must be at least 8 characters long and include at least one letter, one number, and one special character.';
            isValid = false;
        } else {
            passwordError.textContent = '';
        }

        var confirmPasswordInput = document.getElementById('confirm_password');
        var confirmPasswordError = document.getElementById('confirm_password_error');
        if (passwordInput.value !== confirmPasswordInput.value) {
            confirmPasswordError.textContent = 'Passwords do not match.';
            isValid = false;
        } else {
            confirmPasswordError.textContent = '';
        }

        return isValid;
    }

    document.getElementById('user_name').addEventListener('keyup', function() {
        validateUserName();
    });

    document.getElementById('user_email').addEventListener('keyup', function() {
        validateUserEmail();
    });

    document.getElementById('password').addEventListener('keyup', function() {
        validatePassword();
    });

    document.getElementById('confirm_password').addEventListener('keyup', function() {
        validateConfirmPassword();
    });

    function validateUserName() {
        var userNameInput = document.getElementById('user_name');
        var userNameError = document.getElementById('user_name_error');
        if (!/^[a-zA-Z_]{3,20}$/.test(userNameInput.value)) {
            userNameError.textContent = 'Please enter a valid user name (3-20 characters, letters only).';
            return false;
        } else {
            userNameError.textContent = '';
            return true;
        }
    }

    function validateUserEmail() {
        var emailInput = document.getElementById('user_email');
        var emailError = document.getElementById('user_email_error');
        if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(emailInput.value)) {
            emailError.textContent = 'Please enter a valid email address.';
            return false;
        } else {
            emailError.textContent = '';
            return true;
        }
    }

    function validatePassword() {
        var passwordInput = document.getElementById('password');
        var passwordError = document.getElementById('password_error');
        if (!/(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{8,}/.test(passwordInput.value)) {
            passwordError.textContent = 'Password must be at least 8 characters long and include at least one letter, one number, and one special character.';
            return false;
        } else {
            passwordError.textContent = '';
            return true;
        }
    }

    function validateConfirmPassword() {
        var confirmPasswordInput = document.getElementById('confirm_password');
        var confirmPasswordError = document.getElementById('confirm_password_error');
        if (document.getElementById('password').value !== confirmPasswordInput.value) {
            confirmPasswordError.textContent = 'Passwords do not match.';
            return false;
        } else {
            confirmPasswordError.textContent = '';
            return true;
        }
    }
    document.getElementById('user_name').addEventListener('blur', function() {
        if (validateUserName()) {
            document.getElementById('user_email').disabled = false;
        }
    });

    document.getElementById('user_email').addEventListener('blur', function() {
        if (validateUserEmail()) {
            document.getElementById('password').disabled = false;
        }
    });

    document.getElementById('password').addEventListener('blur', function() {
        if (validatePassword()) {
            document.getElementById('confirm_password').disabled = false;
        }
    });

    document.getElementById('confirm_password').addEventListener('blur', function() {
        validateConfirmPassword();
    });

    </script>
    <script>
        var status = document.getElementById('status').value;
        var message = document.getElementById('message').value;

        if (status === "failure") {
            Swal.fire({
                icon: 'error',
                title: 'Registration Failed!',
                text: message
            });
        }
    </script>
</body>
</html>
