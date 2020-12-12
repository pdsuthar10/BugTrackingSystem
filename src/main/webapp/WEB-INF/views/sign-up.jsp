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
</head>
<body>
<h1>Registration Page!</h1>
<form method="post" action="register">
    <label for="name">Name: </label><input id="name" type="text" name="name">
    <label for="username">Username: </label><input id="username" type="text" name="username"><br/>
    <label for="password">Password: </label><input id="password" type="password" name="password"><br/>
    <label for="confirmPassword">Confirm Password: </label><input id="confirmPassword" type="password" name="confirmPassword"><br/>
    <input type="submit" value="Sign up">
</form>

</body>
</html>
