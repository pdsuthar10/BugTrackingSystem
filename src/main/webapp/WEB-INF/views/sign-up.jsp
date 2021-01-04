<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <div class="container shadow-lg p-3 mb-5 bg-white rounded" style="margin-top: 40px">
        <h1 style="text-align: center">Registration</h1>
        <form:form modelAttribute="user" method="post" action="register">
            <div class="mb-3">
                <label for="name" class="form-label">Name: </label>
                <form:input path="name" id="name" name="name" class="form-control"/>
                <span style="color: red; font-style: italic"><b><form:errors path="name" cssClass="error"/></b></span>
            </div>
            <div class="mb-3">
                <label for="username" class="form-label">Username: </label>
                <form:input path="username" id="username" name="username" class="form-control"/>
                <span style="color: red; font-style: italic"><b><form:errors path="username" cssClass="error"/></b></span>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password: </label>
                <form:password path="password" id="password" name="password" class="form-control"/>
                <span style="color: red; font-style: italic"><b><form:errors path="password" cssClass="error"/></b></span>
            </div>
            <div class="mb-3">
                <label for="confirmPassword" class="form-label">Confirm Password: </label>
                <input id="confirmPassword" type="password" name="confirmPassword" class="form-control">
                <span style="color: red; font-style: italic"><b>${error}</b></span>
            </div>

            <button type="submit" class="btn btn-success">Sign Up</button>
        </form:form>
        <a href="/bts/"><button class="btn btn-primary">Login</button></a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</body>
</html>
