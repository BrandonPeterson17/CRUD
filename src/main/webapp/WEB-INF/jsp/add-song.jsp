<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/31/2017
  Time: 1:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Song</title>
</head>
<body>
<h1>Add Song</h1>
<c:forEach items="${inputErrors}" var="errormsg">
    <p class="errormsg">Error : <c:out value="${errormsg}" /></p>
</c:forEach>
<form:form id="add-album" method="post" action="/add/song" commandName="songForm">
    <table>
        <tr>
            <td>Song:</td>
            <td><form:input path="title" /></td>
            <td rowspan="3"><input id="add_btn" type="submit" value="Add Album" /></td>
            <td rowspan="3"><a id="home_btn" href="/"><input type="button" value="Home"/></a></td>
        </tr>
        <tr>
            <td>Genre:</td>
            <td><form:input path="genre" /></td>
        </tr>
        <tr>
            <td>Rating:</td>
            <td><form:input path="rating" /></td>
        </tr>
        <tr>
            <td>Album:</td>
            <td><form:select path="id"> <!--album id, not song id-->
                <c:forEach items="${albums}" var="album">
                    <form:option label="${album.getTitle()} (${album.getArtistEntity().getArtist()})" value="${album.getId()}" />
                </c:forEach>
            </form:select></td>
        </tr>
    </table>
</form:form>
</body>
</html>