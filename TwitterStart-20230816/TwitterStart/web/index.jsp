<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="twitter.Tweet" %>
<%@ page import="twitter.Tweets" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles.css">
    <title>Home</title>
</head>
<body>
    <header>
        <h1>Bad Twitter</h1>
        <div class="login-container">
            <% 
               String currentUsername = (String) session.getAttribute("username");
               if (currentUsername != null) { 
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
            <li><a href="#">Home</a></li>
            <% if (currentUsername != null) { %>
                <li><a href="Profile">Profile</a></li>
            <% } %>
        </ul>
    </nav>
   
    <% 
        String userQuery = request.getParameter("username");
        List<Tweet> tweets;

        if (currentUsername != null && (userQuery == null || userQuery.trim().isEmpty())) {
            // only tweets of the user and who they follow
            tweets = Tweets.getTweetsForUserAndFollowing(currentUsername);
        } else if (userQuery != null && !userQuery.trim().isEmpty()) {
            // fetch tweets for specific user
            tweets = Tweets.getTweetsByUsername(userQuery);
        } else {
            // Default fetch all tweets.
            tweets = Tweets.getAllTweets();
        }
    %>
    
    <h2><%=currentUsername %></h2>
    
    <% if (currentUsername != null && (userQuery == null || userQuery.trim().isEmpty())) { %>
        <div class="tweet-box">
            <form action="PostTweet" method="post">
                <textarea name="tweetContent" placeholder="What's happening?"></textarea>
                <button type="submit">Tweet</button>
            </form>
        </div>
    <% } %>
    
    <% 
        if (currentUsername != null && userQuery != null && !userQuery.equals(currentUsername)) {
           if (twitter.UserModel.isFollowing((int) request.getSession().getAttribute("user_id"), twitter.UserModel.getUser(userQuery).getId())) {
     %>
              <div class="unfollow-box">
                 <form action="UnfollowUser" method="get">
                     <input type="hidden" name="unfollowUserId" value="<%= twitter.UserModel.getUser(userQuery).getId() %>">
                     <button type="submit">Unfollow <%= userQuery %></button>
                 </form>
              </div>
     <% 
           } else {
     %>
              <div class="follow-box">
                 <form action="FollowUser" method="get">
                     <input type="hidden" name="followUserId" value="<%= twitter.UserModel.getUser(userQuery).getId() %>">
                     <button type="submit">Follow <%= userQuery %></button>
                 </form>
              </div>
     <% 
           }
        } 
     %>
    
    <!-- Loop through the tweets and display them -->
    <div class="tweets">
        <% for (Tweet tweet : tweets) { %>
            <div class="tweet">
                <div class="tweet-header"> <!-- Add this wrapping div for user details -->
                    <img src="<%= tweet.getBase64Image() %>" alt="User Image">
                    <strong><%= tweet.get_user_name() %>:</strong>
                    <% if (currentUsername != null) { 
                        int currentUserId = (int) request.getSession().getAttribute("user_id");
                        if (twitter.UserModel.hasUserLiked(currentUserId, tweet.getId())) { %>
                             <form action="UnlikeTweet" method="post">
                                 <input type="hidden" name="tweetId" value="<%= tweet.getId() %>">
                                 <button type="submit">Unlike</button>
                             </form>
                        <% } else { %>
                             <form action="LikeTweet" method="post">
                                 <input type="hidden" name="tweetId" value="<%= tweet.getId() %>">
                                 <button type="submit">Like</button>
                             </form>
                        <% } 
                     } %>
                     <span class="likes-count"><%= tweet.getLikesCount() %> likes</span>
                </div>
                <p class="tweet-text"><%= tweet.getText() %></p>
                <span><%= tweet.getTimestamp().toString() %></span>
            </div>
        <% } %>
    </div>
    
</body>
</html>
