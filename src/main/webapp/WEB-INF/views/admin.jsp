<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/6/20
  Time: 2:38 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
<h1>Admin Dashboard</h1>
<h3>Admin Name: ${user.name}</h3>
<ul>
    <li><a href="admin/create-project">Create a Project</a></li>
    <li><a href="logout">Log out</a></li>
</ul>

    <c:if test="${projects.size()>0}">
        <h3>Projects:</h3>
        <table border="1px solid dark">
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
                        <a href="/bts/admin/project/${project.id}/issues">Report an Issue</a>&nbsp;|&nbsp;
                        <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(project)}">
                            <a href="/bts/project/${project.id}/add-developers">Add developers</a>&nbsp;|&nbsp;
                        </c:if>
                        <a href="/bts/project/${project.id}/details">Details</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <br/>
    <c:if test="${issues.size()>0}">
        <h4>Issues/Tickets:</h4>
        <table border="1px solid dark">
            <thead>
            <tr>
                <td>Issue-ID</td>
                <td>Project-ID</td>
                <td>Title</td>
                <td>Description</td>
                <td>Status</td>
                <td>Type</td>
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
                            &nbsp;|&nbsp;<a href="">Edit</a>
                        </c:if>
                        <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(issue.project)}">
                            &nbsp;|&nbsp;<a href="">Delete</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</body>
</html>
