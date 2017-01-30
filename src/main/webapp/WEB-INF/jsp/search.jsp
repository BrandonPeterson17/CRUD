<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/12/2017
  Time: 1:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search</title>
</head>
<body>
    <h1>Search</h1>
    <table>
        <tr><form:form method="post" action="/search/?page=0" commandName="songForm">
            <td>Search Here:</td>
            <td><form:input id="title_entry" path="title"/></td>
            <td colspan="2"><input class="btn" type="submit" value="Search"/></td></form:form>
            <form method="post" action="/"><td><button class="btn"/>Home</td></form>
        </tr>
        <tr>
            <td>ID</td>
            <td>Title</td>
            <td>Artist</td>
            <td>Genre</td>
            <td>Rating</td>
        </tr>
        <c:forEach items = "${searchSong.content}" var = "search">
            <tr>
                <td>${search.id}</td>
                <td>${search.title}</td>
                <td>${search.artist}</td>
                <td>${search.genre}</td>
                <td>${search.rating}</td>
            </tr>
        </c:forEach>
        <tr>
            <td class="nav_back"><form:form method="post" action="/search/page/?page=${(param.page > 0) ? param.page - 1 : 0}&title=${songForm.getTitle()}" commandName="songForm"><form:button text="<<<" class="nav">&lt;&lt;&lt;</form:button></form:form></td>
            <td colspan="3">Page ${param.page + 1}/${searchSong.getTotalPages()}</td>
            <td class="nav_back"><form:form method="post" action="/search/page/?page=${param.page <= searchSong.getTotalPages() - 2 ? param.page + 1 : param.page}&title=${songForm.getTitle()}" commandName="songForm"><form:button value=">>>" class="nav">&gt;&gt;&gt;</form:button></form:form></td>
        </tr>
    </table>
</body>
</html>

<style type="text/css">
    html {
        font-family: "Agency FB";
    }

    button {
        font-family: "Agency FB";
        font-size: large;
        font-weight: bold;
    }

    input {
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
    }

    table {
        border: 2px solid blue;
        margin: auto;
    }

    body > * {
        text-align: center;
    }

    td {
        border-bottom: 1px dotted black;
        border-right: 1px dotted black;
    }

    .nav {
        font-size: 3em;
        color: white;
        text-decoration: none;
        line-height: 1em;
        background-color: blue;
        border: 2px solid black;
        margin-bottom: -0.3em;
    }

    .nav_back {

    }

    .btn {
        height: 24px;
        line-height: 0;
        width: 100%;
        font-size: 1.2em;
    }
</style>