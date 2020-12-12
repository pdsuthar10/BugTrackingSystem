<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/8/20
  Time: 7:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Issue</title>
</head>
<body>
<h1>Create issue for Project - ${project.name}</h1>
<ul>
    <li><a href="/bts/user/dashboard">Dashboard</a></li>
</ul>
<c:choose>
    <c:when test="${developers == null || developers.size() == 0}">
        <h3 style="color: red"><b>No developers are assigned for the project. Please contact ${manager.username}!</b></h3>
    </c:when>
    <c:otherwise>
        <form action="/bts/project/${project.id}/issues/submit-issue" method="post">
            <label for="title">Issue Title: </label>
            <input type="text" id="title" name="title" required><br/><br/>
            <c:if test='${error.get("title") != null}'>
                <p><span style="color: red"><b>${error.get("title")}</b></span></p>
            </c:if>

            <label for="description">Description: </label>
            <textarea id="description" name="description" rows="5" cols="60" required></textarea><br/><br/>
            <c:if test='${error.get("description") != null}'>
                <p><span style="color: red"><b>${error.get("description")}</b></span></p>
            </c:if>

            <label for="priority">Priority: </label>
            <select name="priority" id="priority">
                <option value="Severe">Severe</option>
                <option value="High">High</option>
                <option value="General">General</option>
                <option value="Low">Low</option>
            </select>
            <c:if test='${error.get("priority") != null}'>
                <p><span style="color: red"><b>${error.get("priority")}</b></span></p>
            </c:if>

            <label for="issueType">Issue Type: </label>
            <select name="issueType" id="issueType">
                <option value="Bug">Bug</option>
                <option value="Error">Error</option>
                <option value="Task">Task</option>
            </select>
            <c:if test='${error.get("issueType") != null}'>
                <p><span style="color: red"><b>${error.get("issueType")}</b></span></p>
            </c:if>

            <label for="assignTo">Assign To:</label>
            <select name="assignTo" id="assignTo">
                <c:forEach var="developer" items="${developers}">
                    <option value="${developer.username}">${developer.name} (${developer.username})</option>
                </c:forEach>
            </select>
            <c:if test='${error.get("assignTo") != null}'>
                <p><span style="color: red"><b>${error.get("assignTo")}</b></span></p>
            </c:if>

            <input type="submit" value="Create">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>
