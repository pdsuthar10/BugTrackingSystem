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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container shadow p-3 mb-5 bg-white rounded">
    <h1>Issue Details for Issue ID: ${issue.id}</h1>
    <ul>
    <%--    <li><a>Add a comment</a></li>--%>
        <li><a href="/bts/user/dashboard">Dashboard</a></li>
    </ul>
    <h4>ISSUE</h4>
    <ul id="issueUL">
        <li>Title: ${issue.title}</li>
        <li>Description: ${issue.description}</li>
        <li id="status">Status: ${issue.status.name}</li>
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
    <!-- Button trigger modal -->
    <c:if test='${issue.status.name.equals("open")}'>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#commentModal">
            Add a comment
        </button>
    </c:if>
    <c:if test='${(sessionScope.user.admin || sessionScope.user.username.equals(issue.project.manager.username)
     || sessionScope.user.username.equals(issue.assignedTo.username))
            && issue.status.name.equals("open")}'>
        <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#resolveModal">
            Resolve Issue
        </button>
    </c:if>

    <!-- Modal -->
    <div class="modal fade" id="commentModal" tabindex="-1" aria-labelledby="commentModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="commentModalLabel">Add comment</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="commentForm">
                        <div class="mb-3">
                            <label for="comment" class="form-label">Comment</label>
                            <input type="text" class="form-control" id="comment" required>
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="sendComment(${issue.id})">Add comment</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeModal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="resolveModal" tabindex="-1" aria-labelledby="resolveModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="resolveModalLabel">Resolve Issue</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="resolveForm" action="/bts/issues/${issue.id}/resolve" method="post">
                        <div class="mb-3">
                            <label for="resolutionSummary" class="form-label">Resolution Summary</label>
                            <textarea type="text" class="form-control" id="resolutionSummary" name="resolutionSummary" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Resolve</button>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeModalResolve">Close</button>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${issue.comments.size() > 0}">
        <table id="commentsTable" class="table table-striped table-hover">
            <thead>
            <tr>
                <th>Comment ID</th>
                <th>Comment</th>
                <th>Commented By</th>
                <th>Created On</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="comment" items="${issue.comments}">
                <tr>
                    <td>${comment.id}</td>
                    <td>${comment.comment}</td>
                    <td>${comment.commentedBy.name} (${comment.commentedBy.username})</td>
                    <td>${comment.createdOn.toLocaleString()}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/issues.js"></script>
</div>
</body>
</html>
