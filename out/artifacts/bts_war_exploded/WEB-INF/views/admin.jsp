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
<c:if test='${projects.size()>0}'>
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
                <td>${managerMap.get(project.id).name}</td>
                <td>${managerMap.get(project.id).username}</td>
                <td><fmt:formatDate value="${project.startDate}" pattern="MM-dd-yyyy"/></td>
                <td><fmt:formatDate value="${project.targetEndDate}" pattern="MM-dd-yyyy"/></td>
                <td>${project.createdOn.toLocaleString()}</td>
                <td>${project.modifiedOn.toLocaleString()}</td>
                <td><a href="admin/edit-project/${project.id}">Edit</a>&nbsp;|&nbsp;
                    <a href="admin/delete-project/${project.id}">Delete</a>&nbsp;|&nbsp;
                    <a href="admin/project/${project.id}/issues">Report an Issue</a>&nbsp;|&nbsp;
                    <a href="project/${project.id}/add-developers">Add developers</a>&nbsp;|&nbsp;
                    <a href="project/${project.id}/details">Details</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
</body>
</html>
