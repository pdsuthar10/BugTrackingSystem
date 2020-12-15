<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/13/20
  Time: 1:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Issues Assigned</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<div class="container">
<h1>Issues Assigned</h1>
<ul>
    <li><a href="/bts/user/dashboard">Dashboard</a></li>
</ul>
    <div class="shadow p-3 mb-5 bg-white rounded">
        <div class="row">
            <div class="col">
                <h4>Tickets for you:</h4>
            </div>
            <div class="col" style="text-align: right">
                    <select name="filter" id="filter" onchange="issuesFilterChange()">
                        <option value="assigned">Assigned to you</option>
                        <option value="opened">Opened by you</option>
                        <option value="all">All issues related to you</option>
                    </select>
            </div>
        </div>
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
                            &nbsp;|&nbsp;<a href="">Edit</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/issues.js"></script>
</body>
</html>
