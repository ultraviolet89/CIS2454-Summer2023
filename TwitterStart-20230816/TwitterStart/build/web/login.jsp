<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="styles.css">
        <title>Sign In</title>
    </head>
    <body>
        <header>
            <h1><a href="/TwitterStart/">Bad Twitter</a></h1>
        </header>
        <h2>${message}</h2>
        
        <div class="form-container">
            <h2>Welcome to Bad Twitter</h2>
            <form action="Login" method="POST">
                <div class="input-container">
                    <label for="username" class="label">User Name</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="input-container">
                    <label for="password" class="label">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <div class="input-container radio-group">
                    <input type="radio" id="signIn" name="action" value="login" checked>
                    <label for="signIn" class="label">Sign In</label>
                    <input type="radio" id="register" name="action" value="register">
                    <label for="register" class="label">Register</label>
                </div>
                <button type="submit">Submit</button>
            </form>
        </div>
    </body>
</html>
