<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/23/2017
  Time: 3:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Email Song</title>
</head>
<body>
    <h1>Send an Email of Your Song!</h1>
    <form id="email" method="post" action="/send/${emailSong.id}" commandName="emailForm">
        <table>
            <tr>
                <td colspan="3">Enter your email and get your song in an email!</td>
                <td rowspan="3"><input type="submit" /></td>
            </tr>
            <tr>
                <td>Email: </td>
                <td colspan="2"><input type="text" name="emailIn" placeholder="<Enter email here>" value=""/></td>
            </tr>
            <tr>
                <td colspan="3">Song stats</td>
            </tr>
            <tr>
                <td>${emailSong.title}</td>
                <td>${emailSong.artist}</td>
                <td>${emailSong.genre}</td>
                <td>${emailSong.rating}</td>
            </tr>
        </table>
    </form>
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

    td {
        border-bottom: 1px dotted black;
        border-right: 1px dotted black;
    }

    .errormsg {
        color: red;
    }

    .nav {
        font-size: 10em;
        color: white;
        text-decoration: none;
    }

    .nav_back {
        background-color: blue;
        border: 2px solid black;
    }

    #add, #search {
        height: 24px;
        line-height: 0;
        width: 100%;
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
    }

    #add {
        font-size: 2em;
    }

    #search {
        font-size: 1.2em;
    }
</style>
