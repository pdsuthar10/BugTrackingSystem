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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<div class="container">
<h1>Admin Dashboard</h1>
<h3>Admin Name: ${user.name}</h3>
<ul>
    <li><a href="admin/create-project">Create a Project</a></li>
    <li><a href="logout">Log out</a></li>
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
        </table>
        </div>
    </c:if>
    <br/>
    <c:if test="${issues.size()>0}">
        <div class="shadow p-3 mb-5 bg-white rounded">
        <h4>Issues/Tickets:</h4>
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>Issue-ID</th>
                <th>Project-ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Status</th>
                <th>Type</th>
                <th>Priority</th>
                <th>Opened By</th>
                <th>Assigned To</th>
                <th>Created On</th>
                <th>Modified On</th>
                <th>Action</th>
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
                        <br/>
                        <c:if test="${sessionScope.user.admin || sessionScope.user.assignedIssue(issue)
                            || sessionScope.user.isManagerForProject(issue.project) }">
                            &nbsp;|&nbsp;<a href="/bts/project/${issue.project.id}/issues/${issue.id}/edit-issue">Edit</a>
                            <br/>
                        </c:if>
                        <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(issue.project)}">
                            &nbsp;|&nbsp;<a href="">Delete</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        </div>
        <!-- Modal -->
<%--        <div class="modal fade" id="editIssueModal" tabindex="-1" aria-labelledby="editIssueModalLabel" aria-hidden="true">--%>
<%--            <div class="modal-dialog modal-dialog-centered">--%>
<%--                <div class="modal-content">--%>
<%--                    <div class="modal-header">--%>
<%--                        <h5 class="modal-title" id="editIssueModalLabel">Edit Issue</h5>--%>
<%--                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>--%>
<%--                    </div>--%>
<%--                    <div class="modal-body">--%>
<%--                        <form id="editIssueForm">--%>
<%--                            <div class="mb-3">--%>
<%--                                <label for="title" class="form-label">Title</label>--%>
<%--                                <input type="text" class="form-control" id="title" disabled value="">--%>
<%--                            </div>--%>
<%--                            <button type="submit" class="btn btn-primary" onclick="sendComment(${issue.id})">Add comment</button>--%>
<%--                        </form>--%>
<%--                    </div>--%>
<%--                    <div class="modal-footer">--%>
<%--                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeModal">Close</button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
    </c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/issues.js"></script>
</body>
</html>
