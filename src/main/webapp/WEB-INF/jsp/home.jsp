<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Music Organizer Plus</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <spring:url value="/resources/home.js" var="homeJS"/>
    <script src="${homeJS}"></script>
</head>
<body>
    <h1>Music Organizer Plus</h1>
    <h3>By Brandon Peterson</h3>
    <div id="errmsgs"></div>
    <c:forEach items="${inputErrors}" var="errormsg">
        <p class="errormsg">Error : <c:out value="${errormsg}"/></p>
    </c:forEach>
    <table cellpadding="5px">
        <tr>
            <td rowspan="8" class="nav_back" ><a class="nav" href="/home/?page=${(param.page > 0) ? param.page - 1 : 0}">&lt;</a></td>
            <td colspan="7" align="center">SONGS</td>
            <td colspan="2" align="center">EDIT</td>
            <td align="center">EMAIL</td>
            <td rowspan="8" class="nav_back" ><a class="nav" href="/home/?page=${(param.page < totalPages - 1) ? param.page + 1 : param.page}">&gt;</a></td>
        </tr>
        <tr>
            <td><form action="/sortBy"><input type="submit" name="input" value="Song Num"></form></td>
            <td><form action="/sortBy"><input type="submit" name="input" value="Title"></form></td>
            <td><form action="/sortBy"><input type="submit" name="input" value="Artist"></form></td>
            <td><form action="/sortBy"><input type="submit" name="input" value="Album"></form></td>
            <td><form action="/sortBy"><input type="submit" name="input" value="Release Date"></form></td>
            <td><form action="/sortBy"><input type="submit" name="input" value="Genre"></form></td>
            <td><form action="/sortBy"><input type="submit" name="input" value="Rating"></form></td>
            <td>Edit</td>
            <td>Remove</td>
            <td>Select</td>
        </tr>
        <script type="text/javascript">
            function getAlbums(options) {
                var settings = $.extend({
                    'songId': '1',
                    'artistId': '-1'
                }, options);
                if(settings.artistId === '-1') {
                    settings.artistId = $('#artist_drop_' + settings.songId).val();
                }
                $.ajax('/getAlbums', {
                    data: {'artistId': settings.artistId,
                        'songId': settings.songId},
                    method: 'POST',
                    success: function(result) {
                        if(result == '') {}
                        $("#album_drop_" + settings.songId).html(result);
                    },
                    error: function (error) {
                        $("#album_drop_" + settings.songId).html(error);
                    }
                });
            }

            function requestEdit(options) {
                var id = options.songId;
                var row = $('#row_' + id);
                var message = '';
                $.ajax('/edit', {
                    success: function(result) {
                        if(result == "") {
                            message = 'Save Successful!';
                        } else {
                            $('#errmsgs').html(result).slideDown();
                            message = 'Invalid Input...';
                        }
                    },
                    data: {
                        'songId': id,
                        'page': options.page,
                        'title': row.find('.edit_title').val(),
                        'artistId': row.find('.artist_drop').val(),
                        'albumId': row.find('.album_drop').val(),
                        'date': row.find('.edit_date').val(),
                        'genre': row.find('.edit_genre').val(),
                        'rating': row.find('.edit_rating').val()
                    },
                    method: 'POST',
                    beforeSend: function () {
                        $('#projectEuler').html('Saving...').fadeIn();
                        $('#errmsgs').slideUp();
                    },
                    complete: function () {
                        $('#projectEuler').html(message).fadeOut();
                    },
                    fail: function (error) {
                        alert('uh oh...');
                        message = 'Something Went Wrong...';
                    }
                });
            }

        </script>
        <c:forEach items = "${songTry}" var ="song" begin="${param.page * 5}" end="${(param.page * 5) + 4}" step="1">
        <tr id="row_${song.id}">

            <form id="edit_form_${song.id}" class="edit_form" method="post"> <!--onsubmit="requestEdit({'page': S{param.page}, 'songId': S{songDTO.id}});">-->
            <td>${song.id}</td>
            <td><input id="edit_title_${song.id}" class="edit_title" value="${song.title}"/></td>
                <!--onchange="S{updateAlbums(songDTO.albumEntity.getArtistEntity().getId())}" -->
            <td><select onchange="getAlbums({'songId': '${song.id}'})" id="artist_drop_${song.id}" class="artist_drop">
                <c:forEach items="${artistRepo}" var="album">
                    <c:if test="${album.getId() == song.albumEntity.getArtistEntity().getId()}"> <!--finds only matching artist to put first-->
                        <option value="${album.getId()}">${album.getArtist()}</option>
                    </c:if>
                </c:forEach>
                <c:forEach items="${artistRepo}" var="album">
                    <c:if test="${album.getId() != song.albumEntity.getArtistEntity().getId() && album.getAlbumEntities().size()>0}"> <!--finds only matching artist to put first-->
                        <option value="${album.getId()}">${album.getArtist()}</option>
                    </c:if>

                </c:forEach>
            </select></td>

            <td ><select id="album_drop_${song.id}" class="album_drop"></select></td>

            <script type="text/javascript">
                $(document).ready(function () {
                    getAlbums({'songId': "${song.id}",'artistId': "${song.albumEntity.getArtistEntity().getId()}"});
                });
            </script>

            <td><input id="edit_date_${song.id}" class="edit_date" value="${song.albumEntity.getDate()}" size="8"/></td>
            <td><input id="edit_genre_${song.id}" class="edit_genre" value="${song.genre}" size="15"/></td>
            <td><input id="edit_rating_${song.id}" class="edit_rating" value="${song.rating}" size="4"/></td>


                <td><button class="edit_submit" type="button" onclick="requestEdit({'page': ${param.page}, 'songId': ${song.id}})" >Save</button></td>
            </form>
            <td><form class="delete" method="post" action="/delete/${song.id}?page=${param.page}"><input class="delete_btn" type="submit" value="Delete" /></form></td>
            <td><form class="email" method="post" action="/email/${song.id}"><input class="email_btn" type="submit" value="Email"/></form></td>
        </tr>
        </c:forEach>
        <tr>
            <td colspan="2"><a href="/addsong"><button class="add">Add Song</button></a></td>
            <td colspan="1"><a href="/addartist"><button class="add">Add Artist</button></a></td>
            <td colspan="1"><a href="/addalbum"><button class="add">Add Album</button></a></td>
            <td colspan="2"><a href="/album/?page=0"><button id="album">Album View</button></a></td>
            <td colspan="2"><a href="/artist/?page=0"><button id="artist">Artist View</button></a></td>
            <td colspan="2"><a href="/search"><button id="search">Search</button></a></td>
        </tr>
    </table>
    <h1 id="projectEuler">${projectEuler}</h1>
    <h2 id="aboutThat__"></h2>
    <img src="https://s-media-cache-ak0.pinimg.com/originals/8a/a9/cf/8aa9cffc165fa17bb74c5ddae2266a94.gif"/>

    <script type="text/javascript">
        function sortBy(type) {
            $.ajax('/sortBy', {
                data: {'type': type},
                method: 'POST',
                success: function (woohoo) {
                    console.log(woohoo);
                    location.reload(true);
                }
            });
        }
    </script>
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

    .add, #search, #album, #artist {
        height: 24px;
        width: 100%;
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
        font-size: 1.2em;
    }
</style>
















