<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/8/20
  Time: 2:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit project</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container">
<h1>Edit Project Details</h1>
<form:form modelAttribute="project" action="/bts/admin/edit-project/${project.id}/submit" method="post">
    <label for="name">Project Name: </label><form:input path="name" /><br/>
    <p><span style="color: red; font-style: italic"><b><form:errors path="name"/></b></span></p>

    <label for="description">Description: </label><form:textarea path="description" rows="5" cols="60" /><br/>
    <p><span style="color: red; font-style: italic"><b><form:errors path="description"/></b></span></p>

    <label for="startDate">Start Date: </label><form:input type="date" path="startDate"/><br/>
    <p><span style="color: red; font-style: italic"><b><form:errors path="startDate"/></b></span></p>

    <label for="targetEndDate">Target End Date: </label><form:input type="date" path="targetEndDate"/><br/>
    <p><span style="color: red; font-style: italic"><b><form:errors path="targetEndDate"/></b></span></p>

    <label for="manager">Manager: </label>
    <select id="manager" name="manager">
        <c:forEach var="user" items="${users}">
            <c:choose>
                <c:when test="${user.userId == manager.userId}">
                    <option value="${user.username}" selected>${user.name}</option>
                </c:when>
                <c:otherwise>
                    <option value="${user.username}">${user.name}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>
    <br/><br/>
    <c:if test='${error.get("manager") != null}'>
        <p><span style="color: red"><b>${error.get("manager")}</b></span></p>
    </c:if>

    <input type="submit" value="Edit Project">
</form:form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</body>
</html>
