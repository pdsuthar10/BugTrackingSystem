<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

<h1>Login Page!</h1>
<form method="post" action="checkForLogin">
	<label for="username">Username: </label><input id="username" type="text" name="username"><br/>
	<label for="password">Password: </label><input id="password" type="password" name="password"><br/>
	<input type="submit" value="Login">
	<a href="sign-up.htm">Click here to sign up</a>
</form>

</body>
</html>