<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/30/2017
  Time: 4:59 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Artist</title>
</head>
<body>
<h1>Add an Artist</h1>
<c:forEach items="${inputErrors}" var="errormsg">
    <p class="errormsg">Error : <c:out value="${errormsg}"/></p>
</c:forEach>
    <form:form id="add" method="post" action="/add/artist" commandName="songForm">
        <table>
            <tr>
                <th>Artist:</th>
                <th><form:input path="artist" /></th>
                <th><input id="add_btn" type="submit" value="Add Artist"/></th>
                <th><a id="home_button" href="/"><input type="button" value="Home"/></a></th>
            </tr>
        </table>
    </form:form>
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
