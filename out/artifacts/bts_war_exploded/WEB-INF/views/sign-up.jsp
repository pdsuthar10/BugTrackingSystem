<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 11/29/20
  Time: 3:00 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up!</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<div class="container">
<h1>Registration Page!</h1>
<form method="post" action="register">
    <label for="name">Name: </label><input id="name" type="text" name="name">
    <label for="username">Username: </label><input id="username" type="text" name="username"><br/>
    <label for="password">Password: </label><input id="password" type="password" name="password"><br/>
    <label for="confirmPassword">Confirm Password: </label><input id="confirmPassword" type="password" name="confirmPassword"><br/>
    <input type="submit" value="Sign up">
</form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</body>
</html>
