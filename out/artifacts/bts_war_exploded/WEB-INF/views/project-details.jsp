<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/8/20
  Time: 11:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Project Details</title>
</head>
<body>
<h1>Project Details</h1>
<ul>
    <li>Project ID: ${project.id}</li>
    <li>Name: ${project.name}</li>
    <li>Description: ${project.description}</li>
</ul>
<h4>Manager Details:</h4>
<ul>
    <li>Name: ${manager.name}</li>
    <li>Username: ${manager.username}</li>
</ul>
<h4>Assigned Developers:</h4>
<ul>
    <c:forEach var="developer" items="${developers}">
        <li>${developer.name} (${developer.username})</li>
    </c:forEach>
</ul>
<ul>
    <li><a href="issues/create-issue">Create an issue</a></li>
</ul>
<c:choose>
    <c:when test="${issues.size() == 0}">
        <h4 style="color: green"><b>No issues created for this project!</b></h4>
    </c:when>
    <c:otherwise>
        <h4>Issues/Tickets:</h4>
        <table border="1px solid dark">
            <thead>
                <tr>
                    <td>Issue-ID</td>
                    <td>Title</td>
                    <td>Description</td>
                    <td>Status</td>
                    <td>Priority</td>
                    <td>Opened By</td>
                    <td>Assigned To</td>
                    <td>Created On</td>
                    <td>Modified On</td>
                    <td>Action</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="issue" items="${issues}">
                    <tr>
                        <td>${issue.id}</td>
                        <td>${issue.title}</td>
                        <td>${issue.description}</td>
                        <td>${issue.status.name}</td>
                        <td>${issue.priority.name}</td>
                        <td>${issue.openedBy.name} (${issue.openedBy.username})</td>
                        <td>${issue.assignedTo.name} (${issue.assignedTo.username})</td>
                        <td>${issue.createdOn}</td>
                        <td>${issue.modifiedOn.toLocaleString()}</td>
                        <td>
                            <a href="">View</a>&nbsp;|&nbsp;
                            <c:if test="${sessionScope.user.admin || sessionScope.user.isDeveloperForProject(project)
                            || sessionScope.user.isManagerForProject(project) }">
                                <a href="">Edit</a>&nbsp;|&nbsp;
                            </c:if>
                            <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(project)}">
                                <a href="">Delete</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>
