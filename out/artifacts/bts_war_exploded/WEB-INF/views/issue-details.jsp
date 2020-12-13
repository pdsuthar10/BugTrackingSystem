<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/12/20
  Time: 5:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Issue Details</title>
</head>
<body>
<h1>Issue Details for Issue ID: ${issue.id}</h1>
<ul>
    <li><a>Add a comment</a></li>
    <li><a href="/bts/user/dashboard">Dashboard</a></li>
</ul>
<h4>ISSUE</h4>
<ul>
    <li>Title: ${issue.title}</li>
    <li>Description: ${issue.description}</li>
    <li>Status: ${issue.status.name}</li>
    <li>Priority: ${issue.priority.name}</li>
    <li>Issue Type: ${issue.issueType}</li>
    <li>Generated for Project: ID(${issue.project.id}) Title(${issue.project.name})</li>
    <li>Assigned To: ${issue.assignedTo.name} (${issue.assignedTo.username})</li>
    <li>OpenedBy: ${issue.openedBy.name} (${issue.openedBy.username})</li>
    <li>Created On: ${issue.createdOn.toLocaleString()}</li>
    <li>Modified On: ${issue.modifiedOn.toLocaleString()}</li>
    <c:if test='${issue.status.name.equals("closed")}'>
        <li>Closed On: ${issue.closedOn.toLocaleString()}</li>
        <li>Closed By: ${issue.closedBy.name} (${issue.closedBy.username})</li>
        <li>Resolution Summary: ${issue.resolutionSummary}</li>
    </c:if>
</ul>
<p>Will show a comments table</p>
</body>
</html>
