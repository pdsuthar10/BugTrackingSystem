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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<div class="container shadow p-3 mb-5 bg-white rounded">
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
            <div class="mb-3">
                <label for="title" class="form-label">Issue Title</label>
                <input type="text" class="form-control" id="title" required>
            </div>
            <c:if test='${error.get("title") != null}'>
                <p><span style="color: red"><b>${error.get("title")}</b></span></p>
            </c:if>

            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea id="description" name="description" rows="5" cols="60" required class="form-control"></textarea>
            </div>
            <c:if test='${error.get("description") != null}'>
                <p><span style="color: red"><b>${error.get("description")}</b></span></p>
            </c:if>
            <br/>

            <label for="priority" class="form-label">Priority: </label>
            <select class="form-select" name="priority" id="priority">
                <option value="Severe">Severe</option>
                <option value="High">High</option>
                <option value="General">General</option>
                <option value="Low">Low</option>
            </select>
            <c:if test='${error.get("priority") != null}'>
                <p><span style="color: red"><b>${error.get("priority")}</b></span></p>
            </c:if>
            <br/>

            <label for="issueType" class="form-label">Issue Type: </label>
            <select class="form-select" name="issueType" id="issueType">
                <option value="Bug">Bug</option>
                <option value="Error">Error</option>
                <option value="Task">Task</option>
            </select>
            <c:if test='${error.get("issueType") != null}'>
                <p><span style="color: red"><b>${error.get("issueType")}</b></span></p>
            </c:if>
            <br/>

            <label for="assignTo" class="form-label">Assign To:</label>
            <select class="form-select" name="assignTo" id="assignTo">
                <c:forEach var="developer" items="${developers}">
                    <option value="${developer.username}">${developer.name} (${developer.username})</option>
                </c:forEach>
            </select>
            <c:if test='${error.get("assignTo") != null}'>
                <p><span style="color: red"><b>${error.get("assignTo")}</b></span></p>
            </c:if>
            <br/>

            <button type="submit" class="btn btn-primary">Create</button>
        </form>
    </c:otherwise>
</c:choose>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</body>
</html>
