<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #ffebcd; /* Pale orange background color */
            margin: 0;
        }

        .login-container {
            display: flex;
            width: 100%;
            height: 100%;
        }

        .login-form-container {
            flex: 1;
            padding: 2rem;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #ffffff; /* Left side background color */
        }

        .login-card {
            width: 100%;
            max-width: 400px;
            padding: 2rem;
            background-color: #e1e1e1;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .login-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }

        .login-card h5 {
            background-color: #375864; /* Card header background color (orange) */
            color: #fff;
            text-align: center;
            margin-bottom: 1.5rem;
            padding: 0.75rem;
            border-radius: 5px;
        }

        .login-card .form-group {
            margin-bottom: 1.5rem;
        }

        .login-card .btn-primary,
        .login-card .btn {
            width: 100%;
            background-color: #fe4e5a; /* Button background color (blue) */
            border-color: #007bff;
        }

        .login-card .btn-primary:hover,
        .login-card .btn:hover {
            background-color: #b71d29;
            border-color: #b71d29;
        }

        .image-container {
            flex: 1;
            background-image: url('https://img.freepik.com/free-vector/login-concept-illustration_114360-748.jpg'); /* Replace with your image URL */
            background-size: cover;
            background-position: center;
            height: 100vh;
        }

        .btn {
            display: inline-block;
            margin: 10px 0;
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
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-form-container">
            <div class="login-card">
                
                <form id="loginForm" action="login" method="POST">
                    <div class="form-group">
                        <label for="userId">User ID</label>
                        <input type="text" class="form-control" id="userId" name="userId" required pattern="[0-9]{4}" placeholder="User ID">
                    </div>
                    <input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
                    <input type="hidden" id="message" value="<%= request.getAttribute("message") %>">
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?\" placeholder="Password">
                    </div>
                    <button type="submit" class="btn btn-primary">Login</button>
                </form>
                
                <!-- Register Button -->
                <form class="form" action="register">
                    <button class="btn" type="submit" id="registerBtn">Register
                        <div class="notification">If you are new here, choose me</div>
                    </button>
                </form>
            </div>
        </div>
        <div class="image-container"></div>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var status = document.getElementById('status').value;

            if (status === 'failed') {
                Swal.fire({
                    icon: 'error',
                    title: 'Wrong userID or password!',
                    text: 'Your login failed!'
                });
            } else if (status === 'error') {
                var message = document.getElementById('message').value || 'There was an error processing your request. Please try again later.';
                Swal.fire({
                    icon: 'error',
                    title: 'Server Error',
                    text: message
                });
            }
        });

        const registerBtn = document.getElementById('registerBtn');

        registerBtn.addEventListener('mouseenter', () => {
            showNotification(registerBtn, 'If you are new here, choose me');
        });

        registerBtn.addEventListener('mouseleave', () => {
            hideNotification(registerBtn);
        });

        function showNotification(btn, message) {
            const notification = btn.querySelector('.notification');
            notification.innerText = message;
            notification.style.display = 'block';
            setTimeout(() => {
                notification.style.opacity = '1';
            }, 10);
        }

        function hideNotification(btn) {
            const notification = btn.querySelector('.notification');
            notification.style.opacity = '0';
            setTimeout(() => {
                notification.style.display = 'none';
            }, 300);
        }
    </script>
</body>
</html>
