<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/8/20
  Time: 8:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add developers</title>
</head>
<body>
<h1>Add developers for Project - ${project.name}</h1>
<h4>Manager Details:</h4>
<ul>
    <li>Name: ${manager.name}</li>
    <li>Username: ${manager.username}</li>
</ul>
<c:choose>
    <c:when test="${developers == null || developers.size() == 0}">
        <h4 style="color: red"><b>No developers are assigned to this project yet!</b></h4>
    </c:when>
    <c:otherwise>
        <h4>Assigned Developers:</h4>
        <ul>
            <c:forEach var="developer" items="${developers}">
                <li>${developer.name} (${developer.username})</li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${unassignedDevelopers == null || unassignedDevelopers.size() == 0}">
        <h4 style="color: red"><b>No developers left to assign</b></h4>
    </c:when>
    <c:otherwise>
        <h4>Please select developer(s) to add:</h4>
        <form action="add-developers" method="post">
            <label>Developers: </label>
            <select name="developersSelected" multiple>
                <c:forEach var="d" items="${unassignedDevelopers}">
                    <option value="${d.username}">${d.name} (${d.username})</option>
                </c:forEach>
            </select>
            <input value="Add" type="submit">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
