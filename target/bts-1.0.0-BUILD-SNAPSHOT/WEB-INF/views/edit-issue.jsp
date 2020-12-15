<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/14/20
  Time: 1:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Issue</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<div class="container shadow p-3 mb-5 bg-white rounded">
    <h1>Edit Issue</h1>
    <form action="/bts/project/${project.id}/issues/${issue.id}/submit-change" method="post">
        <div class="mb-3">
            <label for="title" class="form-label">Issue Title</label>
            <input type="text" class="form-control" id="title" disabled value="${issue.title}" required>
        </div>

        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea id="description" name="description" rows="5" cols="60" required class="form-control">${issue.description}</textarea>
        </div>
        <c:if test='${error.get("description") != null}'>
            <p><span style="color: red"><b>${error.get("description")}</b></span></p>
        </c:if>
        <br/>

        <label for="priority" class="form-label">Priority: </label>
        <select class="form-select" name="priority" id="priority">
            <c:forEach var="priority" items="${priorities}">
                <c:choose>
                    <c:when test="${priority.equals(issue.priority.name)}">
                        <option value="${priority}" selected>${priority}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${priority}">${priority}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
        <c:if test='${error.get("priority") != null}'>
            <p><span style="color: red"><b>${error.get("priority")}</b></span></p>
        </c:if>
        <br/>

        <label for="issueType" class="form-label">Issue Type: </label>
        <select class="form-select" name="issueType" id="issueType">
            <c:forEach var="issueType" items="${issueTypes}">
                <c:choose>
                    <c:when test="${issueType.equals(issue.issueType)}">
                        <option value="${issueType}" selected>${issueType}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${issueType}">${issueType}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
        <c:if test='${error.get("issueType") != null}'>
            <p><span style="color: red"><b>${error.get("issueType")}</b></span></p>
        </c:if>
        <br/>

        <label for="assignTo" class="form-label">Assign To:</label>
        <select class="form-select" name="assignTo" id="assignTo">
            <c:forEach var="developer" items="${project.developers}">
                <c:choose>
                    <c:when test="${developer.username.equals(issue.assignedTo.username)}">
                        <option value="${developer.username}" selected>${developer.name} (${developer.username})</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${developer.username}">${developer.name} (${developer.username})</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
        <c:if test='${error.get("assignTo") != null}'>
            <p><span style="color: red"><b>${error.get("assignTo")}</b></span></p>
        </c:if>
        <br/>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>
