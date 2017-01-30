<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/11/2017
  Time: 1:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Song</title>
</head>
<body>
    <h1>Invalid Entry</h1>
    <h4><ul>
        <li>rating must contain no letters or symbols, only numbers are allowed</li>
        <li>rating must be an integer from 0 to 5</li>
    </ul></h4>
    <form method="post" action="/create">
        <button type="submit">&lt;&lt;Back</button>
    </form>
    <p></p>
    <a href="/"><input type="button" value="Home"/></a>
</body>
</html>

<style type="text/css">
    button, input {
        height: 5%;
        font-size: 1.4em;
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
    }

    html {
        font-family: "Agency FB", sans-serif;
    }
</style>
