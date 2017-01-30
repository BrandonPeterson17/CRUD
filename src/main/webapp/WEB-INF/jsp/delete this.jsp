<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/23/2017
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form:form id="email" method="post" action="/send${emailSong.id}" commandName="emailForm">
    <table>
        <tr>
            <td colspan="4">Enter your email and get your song in an email!</td>
        </tr>
        <tr>
            <td>Email: </td>
            <td colspan="3"><form:input path="email" /></td>
        </tr>
        <tr>
            <td>${emailSong.title}</td>
            <td>${emailSong.artist}</td>
            <td>${emailSong.genre}</td>
            <td>${emailSong.rating}</td>
        </tr>
    </table>
</form:form>
</body>
</html>
