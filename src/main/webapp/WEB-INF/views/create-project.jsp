<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: priyamsuthar
  Date: 12/7/20
  Time: 6:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create a Project</title>
</head>
<body>
<h2>Create a Project</h2>
<form:form modelAttribute="project" action="/bts/admin/submit-project" method="post">
    <label for="name">Project Name: </label><form:input path="name"/><br/>
        <p><span style="color: red; font-style: italic"><b><form:errors path="name"/></b></span></p>

    <label for="description">Description: </label><form:textarea path="description" rows="5" cols="60" /><br/>
        <p><span style="color: red; font-style: italic"><b><form:errors path="description"/></b></span></p>

    <label for="startDate">Start Date: </label><form:input type="date" path="startDate"/><br/>
        <p><span style="color: red; font-style: italic"><b><form:errors path="startDate"/></b></span></p>

    <label for="targetEndDate">Target End Date: </label><form:input type="date" path="targetEndDate"/><br/>
    <p><span style="color: red; font-style: italic"><b><form:errors path="targetEndDate"/></b></span></p>

    <label for="manager">Manager: </label>
    <select id="manager" name="manager">
        <c:forEach var="user" items="${users}">
            <option value="${user.username}">${user.name}</option>
        </c:forEach>
    </select>
    <br/><br/>
    <c:if test='${error.get("manager") != null}'>
        <p><span style="color: red"><b>${error.get("manager")}</b></span></p>
    </c:if>

    <input type="submit" value="Create Project">
</form:form>
</body>
</html>
