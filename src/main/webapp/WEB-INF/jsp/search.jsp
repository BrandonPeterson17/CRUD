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
    <!-- Latest compiled and minified CSS -->
    <%--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">--%>
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <%--<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
</head>
<body>
    <h1>Search</h1>
    <table id="search_table">
        <tr><form method="post" onsubmit="loadSongs($('#title_entry').val(), $('#type').val(), 0)">
            <td>Search Here:</td>
            <td><input id="title_entry" name="title"/></td>
            <td><select id="type">
                <option value="song">Song Title</option>
                <option value="album">Album</option>
                <option value="artist">Artist</option>
            </select></td>
            <td colspan="2"><input class="btn" type="submit" value="Search"/></td></form>
            <form method="post" action="/home/?page=0"><td><button class="btn"/>Home</td></form>
        </tr>
        <tr>
            <td>ID</td>
            <td>Title</td>
            <td>Artist</td>
            <td>Album</td>
            <td>Date</td>
            <td>Genre</td>
            <td>Rating</td>
        </tr>


        <%--<c:forEach items = "${searchSong.content}" var = "search">
            <tr>
                <td>${search.id}</td>
                <td>${search.title}</td>
                <td>${search.getArtistEntity().getArtist()}</td>
                <td>${search.getAlbumEntity().getTitle()}</td>
                <td>${search.genre}</td>
                <td>${search.rating}</td>
            </tr>
        </c:forEach>
        <form method="post" action="/search/page/?page=${(param.page > 0) ? param.page - 1 : 0}&title=${songForm.getTitle()}&type=${songForm.getType()}"></form>
        <form method="post" action="/search/page/?page=${param.page <= searchSong.getTotalPages() - 2 ? param.page + 1 : param.page}&title=${songForm.getTitle()}&type=${songForm.getType()}"></form>
        --%>
        <tr class="footer">
            <td class="nav_back"><button onclick="loadSongs($('#title_entry').val(), $('#type').val(), page-1)" class="nav">&lt;&lt;&lt;</button></td>
            <td colspan="4">Page <span id="page_info"></span></td>
            <td class="nav_back"><button onclick="loadSongs($('#title_entry').val(), $('#type').val(), page+1)" class="nav">&gt;&gt;&gt;</button></td>
        </tr>
    </table>
    <h1 id="loading"></h1>
<script type="text/javascript">
    var page = 0;
    var totalPages = 0;
    var pageSize = 5;
    var songPage = [];
    function loadSongs(title, type, pageNum) {
        var songs = [];
        $.ajax('/searchSongs', {
            method: 'POST',
            data: {'title': title,
                'type': type},
            success: function (result) {
//                $('#songs').html(result);
                songs = split(result, ',');
                whenSuccessful();
            },
            fail: function (error) {
                alert('Fail: ' + error);
            },
            beforeSend: function () {
                $('#loading').html('Loading...').fadeIn();
            },
            complete: function () {
                $('#loading').html('Done!').fadeOut();
            },
            timeout: 3000
        });

        function whenSuccessful() {
            console.log('length:' + songs.length);
            console.log('type of songs:' + typeof songs);
            totalPages = parseInt((songs.length + 4) / pageSize);
            console.log('totalPages: ' + totalPages);
            console.log('page: ' + page);
            if(pageNum > totalPages - 1) {
                pageNum = totalPages - 1;
            } else if(pageNum < 0) {
                pageNum = 0;
            }
            page = pageNum;
            console.log('page: ' + page);
            var howToMeetLadies = '';
            songPage = [];
            for(var i=page*5; i<songs.length && i<(page*5)+5; i++) {
                console.log('i=' + i + '; song=' + songs.splice(i));
//                howToMeetLadies += songs.splice(i);
//                songPage.push(songs.splice(i));
                $('.footer').before($(songs.splice(i)));
            }
            $('#page_info').html('' + (page + 1) + '/' + totalPages);
        }

    }

    function getSongRow(rowNum) {
        return songPage.splice(rowNum);
    }

    function split(string, split) {
        var str = '';
        str += string;
        var strings = [];
        for(var i=0; i<str.length; i++) {
            var sub = '';
            var j = i;
            while (str.substring(j, j+1) != split && j<str.length) {
                sub += str.charAt(j++);
            }
            i = j;
            strings.push(sub);
        }
        console.log(strings.splice(0).splice(0));
        return strings;
    }

    $(document).ready(function () {
        loadSongs('', 'song', 0);
    });
</script>
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