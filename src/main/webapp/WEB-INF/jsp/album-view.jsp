<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2/9/2017
  Time: 2:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Album Edit</title>
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
</head>
<body>
<h1>Edit Albums</h1>
<div id="errmsgs"></div>
<table cellpadding="5px">
    <tr>
        <td rowspan="8" class="nav_back" ><a class="nav" href="/album/?page=${(param.page > 0) ? param.page - 1 : 0}">&lt;</a></td>
        <td colspan="4" align="center">ALBUMS</td>
        <td colspan="3" align="center">EDIT</td>
        <td rowspan="8" class="nav_back" ><a class="nav" href="/album/?page=${(param.page < totalAlbumPages - 1) ? param.page + 1 : param.page}">&gt;</a></td>
    </tr>
    <tr>
        <td>Album Num</td>
        <td>Title</td>
        <td>Artist</td>
        <td>Release Date</td>
        <td>Edit</td>
        <td>Remove</td>
    </tr>
    <c:forEach items = "${albums}" var ="album" begin="${param.page * 5}" end="${(param.page * 5) + 4}" step="1">
        <tr id="row_${album.id}">

            <td>${album.id}</td>

            <form id="edit_form_${album.id}" class="edit_form" method="post">

                <td><input id="edit_title_${album.id}" class="edit_title" value="${album.title}" /></td>
                <td><select id="artist_drop_${album.id}" class="artist_drop">
                    <c:forEach items="${artistRepo}" var="artist">
                        <c:if test="${artist.getId() == album.getArtistEntity().getId()}"> <!--finds only matching artist to put first-->
                            <option value="${artist.getId()}">${artist.getArtist()}</option>
                        </c:if>
                    </c:forEach>
                    <c:forEach items="${artistRepo}" var="artist">
                        <c:if test="${artist.getId() != album.getArtistEntity().getId()}"> <!--finds only matching artist to put first-->
                            <option value="${artist.getId()}">${artist.getArtist()}</option>
                        </c:if>
                    </c:forEach>
                </select></td>

                <td><input id="edit_date_${album.id}" class="edit_date" value="${album.date}" size="8"/></td>

                <td><button class="edit_submit" type="button" onclick="requestEdit(${album.id})" >Save</button></td>
            </form>
            <form class="delete" method="post" action="/deleteAlbum/${album.id}/?page=${param.page}"><td><input class="delete_btn" type="submit" value="Delete" /></td></form>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="7"><a href="/"><button class="add">Home</button></a></td>
    </tr>
</table>
<h1 id="projectEuler">${projectEuler}</h1>

<script type="text/javascript">

    function requestEdit(id) {
        var row = $('#row_' + id);
        var message = '';
        console.log('Hey, stuff is happening...');
        $.ajax('/editAlbum', {
            success: function(result) {
                if(result == "") {
                    message = 'Save Successful!';
                    console.log("saveSuccessful");
                } else {
                    $('#errmsgs').html(result).slideDown();
                    message = 'Invalid Input...';
                    console.log('invalidInput');
                }
            },
            data: {
                'albumId': id,
                'title': row.find('.edit_title').val(),
                'artistId': row.find('.artist_drop').val(),
                'date': row.find('.edit_date').val()
            },
            method: 'POST',
            beforeSend: function () {
                $('#projectEuler').html('Saving...').fadeIn();
                $('#errmsgs').slideUp();
                console.log("beforeSend");
            },
            complete: function () {
                $('#projectEuler').html(message).fadeOut();
                console.log("complete");
            },
            fail: function () {
                alert('uh oh...');
                message = 'Something Went Wrong...';
                console.log('fail');
            },
            timeout: 3000
        });
    }
    function deleteAlbum(albumId) {
        if(confirm("Are you sure you want to delete this album and all it's songs?")) {
            $.ajax('/deleteAlbum', {
                data: {'id': albumId,
                    'page': ${param.page}}
            });
        }

    }
</script>

</body>

<style type="text/css">
    html {
        font-family: "Agency FB", sans-serif;
    }

    input {
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
    }

    select {
        width: 100%;
        text-align-last: center;
    }

    input {
        text-align: center;
    }

    img {
        margin-left: 40%;
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

    .edit_input {
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
        text-align: center;
    }

    .add, .edit_submit {
        height: 24px;
        width: 100%;
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
        font-size: 1.2em;
    }
</style>

</html>