<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/31/2017
  Time: 10:38 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Album</title>
</head>
<body>
<h1>Add an Album</h1>
<c:forEach items="${inputErrors}" var="errormsg">
    <p class="errormsg">Error : <c:out value="${errormsg}" /></p>
</c:forEach>
<form:form id="add-album" method="post" action="/add/album" commandName="songForm">
<table>
    <tr>
        <td>Album:</td>
        <td><form:input path="album" /></td>
        <td rowspan="3"><input id="add_btn" type="submit" value="Add Album" /></td>
        <td rowspan="3"><a id="home_btn" href="/"><input type="button" value="Home"/></a></td>
    </tr>
    <tr>
        <td>Release Date:</td>
        <td><form:input path="date" /></td>
    </tr>
    <tr>
        <td>Artist:</td>
        <td><form:select path="artist" items="${artists}"/></td>
    </tr>
</table>
</form:form>
</body>
</html>
