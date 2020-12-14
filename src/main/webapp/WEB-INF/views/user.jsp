<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 11/29/20
  Time: 6:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<div class="container">
<h1>Welcome ${user.username}</h1>
<ul>
    <li><a href="logout">Logout</a></li>
    <c:if test="${user.isDeveloper()}"><li><a href="/bts/user/issuesAssigned">Issues Assigned to you</a></li></c:if>
</ul>
<c:if test="${projects.size()>0}">
    <div class="shadow p-3 mb-5 bg-white rounded">
    <h3>Projects:</h3>
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <td><b>Project-ID</b></td>
            <td><b>Project Name</b></td>
            <td><b>Project Manager</b></td>
            <td><b>Manager Username</b></td>
            <td><b>Start Date</b></td>
            <td><b>Target End Date</b></td>
            <td><b>Created On</b></td>
            <td><b>Modified On</b></td>
            <td><b>Action</b></td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="project" items="${projects}">
            <tr>
                <td>${project.id}</td>
                <td>${project.name}</td>
                <td>${project.manager.name}</td>
                <td>${project.manager.username}</td>
                <td><fmt:formatDate value="${project.startDate}" pattern="MM-dd-yyyy"/></td>
                <td><fmt:formatDate value="${project.targetEndDate}" pattern="MM-dd-yyyy"/></td>
                <td>${project.createdOn.toLocaleString()}</td>
                <td>${project.modifiedOn.toLocaleString()}</td>
                <td>
                    <c:if test="${sessionScope.user.admin}"><a href="/bts/admin/edit-project/${project.id}">Edit</a>&nbsp;|&nbsp;</c:if>
                    <c:if test="${sessionScope.user.admin}"><a href="/bts/admin/delete-project/${project.id}">Delete</a>&nbsp;|&nbsp;</c:if>
                    <a href="/bts/project/${project.id}/issues/create-issue">Report an Issue</a>&nbsp;|&nbsp;
                    <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(project)}">
                        <a href="/bts/project/${project.id}/add-developers">Add developers</a>&nbsp;|&nbsp;
                    </c:if>
                    <a href="/bts/project/${project.id}/details">Details</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table></div>
</c:if>
<br/>
<c:if test="${issues.size()>0}">
    <div class="shadow p-3 mb-5 bg-white rounded">
    <h4>Issues/Tickets:</h4>
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <td><b>Issue-ID</b></td>
            <td><b>Project-ID</b></td>
            <td><b>Title</b></td>
            <td><b>Description</b></td>
            <td><b>Status</b></td>
            <td><b>Type</b></td>
            <td><b>Priority</b></td>
            <td><b>Opened By</b></td>
            <td><b>Assigned To</b></td>
            <td><b>Created On</b></td>
            <td><b>Modified On</b></td>
            <td><b>Action</b></td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="issue" items="${issues}">
            <tr>
                <td>${issue.id}</td>
                <td>${issue.project.id}</td>
                <td>${issue.title}</td>
                <td>${issue.description}</td>
                <td>${issue.status.name}</td>
                <td>${issue.issueType}</td>
                <td>${issue.priority.name}</td>
                <td>${issue.openedBy.name} (${issue.openedBy.username})</td>
                <td>${issue.assignedTo.name} (${issue.assignedTo.username})</td>
                <td>${issue.createdOn}</td>
                <td>${issue.modifiedOn.toLocaleString()}</td>
                <td>
                    <a href="/bts/project/${issue.project.id}/issues/${issue.id}/details">View</a>
                    <c:if test="${sessionScope.user.admin || sessionScope.user.assignedIssue(issue)
                            || sessionScope.user.isManagerForProject(issue.project) }">
                        &nbsp;|&nbsp;<a href="/bts/project/${issue.project.id}/issues/${issue.id}/edit-issue">Edit</a>
                    </c:if>
                    <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(issue.project)}">
                        &nbsp;|&nbsp;<a href="">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table></div>
</c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
</body>
</html>
