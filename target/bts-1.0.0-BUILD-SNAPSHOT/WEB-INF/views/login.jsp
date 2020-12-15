<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">

</head>
<body>
<div class="container shadow-lg p-3 mb-5 bg-white rounded" style="margin-top: 40px">
	<h1 style="text-align: center">Login</h1>
	<form method="post" action="checkForLogin">
		<div class="mb-3">
			<label for="username" class="form-label">Username: </label>
			<input id="username" type="text" name="username" class="form-control">
		</div>
		<div class="mb-3">
			<label for="password" class="form-label">Password: </label>
			<input id="password" type="password" name="password" class="form-control">
		</div>
		<button type="submit" class="btn btn-primary">Login</button>
		<a href="sign-up.htm">Click here to sign up</a><br/>
	</form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</body>
</html>