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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container">
<h1>Project Details</h1>
<c:if test="${projectEnded.length() > 0}">
    <div class="alert alert-danger" role="alert">
        ${projectEnded}
    </div>
</c:if>
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
    <c:if test="${developers == null || developers.size() == 0}">
        <p style="color: red">No developers are assigned for the project!</p>
    </c:if>
    <c:if test="${error != null || error.length() > 0}">
        <div class="alert alert-danger" role="alert">
                ${error}
        </div>
    </c:if>
    <c:if test="${errorDevelopers != null || errorDevelopers.length()>0}">
        <div class="alert alert-danger" role="alert">
            ${errorDevelopers}
        </div>
    </c:if>
    <c:if test="${sessionScope.user.admin || sessionScope.user.username.equals(project.manager.username)}">
        <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addModal">
            Add Developers
        </button>
        <div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addModalLabel">Add developer(s)</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <c:choose>
                            <c:when test="${unassignedDevelopers.size() > 0}">
                                <form action="/bts/project/${project.id}/add-developers" method="post">
                                    <label for="developersSelected" class="form-label">Select developer(s) to add</label>
                                    <select name="developersSelected" multiple id="developersSelected" class="form-select" required>
                                        <c:forEach var="d" items="${unassignedDevelopers}">
                                            <option value="${d.username}">${d.name} (${d.username})</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="btn btn-success">Add</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <p>No developers to add!</p>
                            </c:otherwise>
                        </c:choose>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${developers.size()>0 &&
    (sessionScope.user.admin || sessionScope.user.username.equals(project.manager.username)) }">
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#removeModal">
            Remove Developers
        </button>
        <div class="modal fade" id="removeModal" tabindex="-1" aria-labelledby="removeModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="removeModalLabel">Remove a developer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="/bts/project/${project.id}/remove-developer" method="post">
                            <label for="developerToRemove" class="form-label">Choose one to remove</label>
                            <select name="developerToRemove" class="form-select" id="developerToRemove">
                                <c:forEach var="developer" items="${developers}">
                                    <option value="${developer.username}">${developer.name} (${developer.username})</option>
                                </c:forEach>
                            </select>
                            <button type="submit" class="btn btn-danger">Remove</button>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
<ul>
    <li><a href="issues/create-issue">Create an issue</a></li>
</ul>
<c:choose>
    <c:when test="${issues.size() == 0}">
        <h4 style="color: green"><b>No issues created for this project!</b></h4>
    </c:when>
    <c:otherwise>
        <div class="row">
            <div class="col">
                <h4>Issues/Tickets:</h4>
            </div>
            <div class="col" style="text-align: right">
                <select name="filter" id="filter" onchange="issuesFilterChangeProject('${project.id}','${sessionScope.user.userId}')">
                    <option value="all">All</option>
                    <option value="open">Open</option>
                    <option value="closed">Closed</option>
                </select>
            </div>
        </div>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>Issue-ID</th>
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
            <tbody id="issueList">
                <c:forEach var="issue" items="${issues}">
                    <tr>
                        <td>${issue.id}</td>
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
                            <a href="/bts/project/${project.id}/issues/${issue.id}/details">View</a>
                            <c:if test="${sessionScope.user.admin || sessionScope.user.isDeveloperForProject(project)
                            || sessionScope.user.isManagerForProject(project) }">
                                &nbsp;|&nbsp;<a href="/bts/project/${issue.project.id}/issues/${issue.id}/edit-issue">Edit</a>
                            </c:if>
                            <c:if test="${sessionScope.user.admin || sessionScope.user.isManagerForProject(project)}">
                                &nbsp;|&nbsp;<a href="">Delete</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/issues.js"></script>
</body>
</html>
