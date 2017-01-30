<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/11/2017
  Time: 1:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Song</title>
</head>
<body>
    <h1>Add your song here</h1>
    <c:forEach items="${inputErrors}" var="errormsg">
        <p class="errormsg">Error : <c:out value="${errormsg}"/></p>
    </c:forEach>
    <table>
        <form:form id="add" method="post" action="/add" commandName="songForm">
            <table>
                <tr>
                    <th>Title:</th>
                    <th><form:input path="title"/></th>
                    <th rowspan="2"><input id="add_btn" type="submit" value="Add"/></th>
                </tr>
                <tr>
                    <th>Artist:</th>
                    <th><form:input path="artist" /></th>
                </tr>
                <tr>
                    <th>Genre:</th>
                    <th><form:input path="genre" /></th>
                    <th rowspan="2"><a id="home_button" href="/"><input type="button" value="Home"/></a></th>
                </tr>
                <tr>
                    <th>Rating:</th>
                    <th><form:input path="rating" /></th>
                </tr>
            </table>
        </form:form>
    </table>
</body>
</html>

<style type="text/css">
    html {
        font-family: "Agency FB", sans-serif;
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

    th {
        border-bottom: 1px dotted black;
        border-right: 1px dotted black;
    }

    .errormsg {
        color: red;
    }

    #add {
        height: 24px;
        line-height: 0;
        width: 100%;
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
    }

    #add {
        font-size: 2em;
    }

    #add_btn, #home_button input {
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
        font-size: 20px;
    }
</style>