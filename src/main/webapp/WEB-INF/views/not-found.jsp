<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/14/20
  Time: 1:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Not Found</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/error.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="h-100 row align-items-center">
    <div class="container">
        <h1 class="display-3" style="text-align: center">404: Resource not found</h1>
        <h1 class="display-4" style="text-align: center">Oops!</h1>
    </div>
</div>
</body>
</html>
