<%-- 
    Document   : profile
    Created on : Apr 14, 2022, 6:43:11 PM
    Author     : EricC
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="styles.css">
        <title>Profile</title>
    </head>
    <body>
        <header>
            <h1>Bad Twitter</h1>
            <div class="login-container">
                <% 
                   String username = (String) session.getAttribute("username");
                   if (username != null) { 
                %>
                    <a href="Logout" class="login-btn">Sign Out</a>
                <% 
                   } else { 
                %>
                    <a href="Login" class="login-btn">Sign In</a>
                <% 
                   } 
                %>
            </div>
        </header>
        <nav>
            <ul class="menu">
                <li><a href="/TwitterStart">Home</a></li>
                <% if (username != null) { %>
                    <li><a href="Profile">Profile</a></li>
                <% } %>
            </ul>
        </nav>
        
        <h2>Welcome ${username}!</h2>
        <c:if test="${(filename != null)}">
            <img src="GetImage?username=${username}" width="240" height="300"/>
        </c:if>
        <h3>upload a profile picture!</h3>
        <form action="Upload" method="post" enctype="multipart/form-data">
            <div id="data">
                <input type="file" accept="image/*" name="file">
            </div>
            <div id="buttons">
                <label>&nbsp;</label>
                <input type="submit" value="Upload"><br>
            </div>
        </form>
    </body>
</html>
