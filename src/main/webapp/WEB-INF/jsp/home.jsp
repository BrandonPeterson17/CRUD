<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Music Organizer Plus</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
    <h1>Music Organizer Plus</h1>
    <h3>By Brandon Peterson</h3>
    <c:forEach items="${inputErrors}" var="errormsg">
        <p class="errormsg">Error : <c:out value="${errormsg}"/></p>
    </c:forEach>
    <table cellpadding="5px">
        <tr>
            <td rowspan="8" class="nav_back" ><a class="nav" href="/home/?page=${(param.page > 0) ? param.page - 1 : 0}">&lt;</a></td>
            <td colspan="8" align="center">SONGS</td>
            <td colspan="8" align="center">EDIT</td>
            <td align="center">Email</td>
            <td rowspan="8" class="nav_back" ><a class="nav" href="/home/?page=${(param.page < totalPages - param.page - 1) ? param.page + 1 : param.page}">&gt;</a></td>
        </tr>
        <tr>
            <td>Song Num</td>
            <td>Title</td>
            <td>Artist</td>
            <td>Album</td>
            <td>Release Date</td>
            <td>Genre</td>
            <td>Rating</td>
            <td>Remove</td>
            <td>Edit Title</td>
            <td>Edit Artist</td>
            <td colspan="2">Edit Album</td>
            <td>Edit Date</td>
            <td>Edit Genre</td>
            <td>Edit Rating</td>
            <td>Edit</td>
            <td>Select</td>
        </tr>
        <c:forEach items = "${song.content}" var = "songDTO" begin="${param.page * 5}" end="${(param.page * 5) + 4}">
        <tr>

            <form:form class="edit" method="post" action="/edit/${songDTO.id}?page=${param.page}" commandName="editForm">
            <td>${songDTO.id}</td>
            <td><form:input cssclass="edit_title" path="title" value="${songDTO.title}" /></td>



            <td><form:select cssClass="edit_album" path="albumId" > <!--album id, not song id-->
                <c:forEach items="${songDTO.albumEntity.getArtistEntity().getAlbumEntities()}" var="album">
                    <c:if test="${album.getId() == songDTO.albumEntity.getId()}"> <!--finds only matching album to put first-->
                        <form:option label="${album.getTitle()} (${album.getArtistEntity().getArtist()})" value="${album.getId()}" />
                    </c:if>
                </c:forEach>
                <c:forEach items="${songDTO.albumEntity.getArtistEntity().getAlbumEntities()}" var="album">
                    <c:if test="${album.getId() != songDTO.albumEntity.getId()}"> <!--gets the rest, ignorinng first to avoid duplicates-->
                        <form:option label="${album.getTitle()} (${album.getArtistEntity().getArtist()})" value="${album.getId()}" />
                    </c:if>
                </c:forEach>
            </form:select></td>




            <td><form:input cssClass="edit_date" path="date" value="${songDTO.date}"/></td>
            <td><form:input cssClass="edit_genre" path="genre" value="${songDTO.genre}"/></td>
            <td><form:input cssClass="edit_rating" path="rating" value="${songDTO.rating}"/></td>


                <td><form:button class="edit_submit" type="submit" >Save</form:button></td>
            </form:form>
            <form class="delete" method="post" action="/delete/${songDTO.id}?page=${param.page}"><td><input class="delete_btn" type="submit" value="Delete" /></td></form>
            <form class="email" method="post" action="/email/${songDTO.id}"><td><input class="email_btn" type="submit" value="Email"/></td></form>
        </tr>
        </c:forEach>
        <tr>
            <td colspan="3"><a href="/addsong"><button class="add">Add Song</button></a></td>
            <td colspan="3"><a href="/addalbum"><button class="add">Add Album</button></a></td>
            <td colspan="3"><a href="/addartist"><button class="add">Add Artist</button></a></td>
            <td colspan="8"><a href="/search/page/?page=0&title="><button id="search">Search</button></a></td>
        </tr>
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

    .add, #search {
        height: 24px;
        width: 100%;
        font-family: "Agency FB", sans-serif;
        font-weight: bold;
    }

    .add {
        font-size: 1.2em;
    }

    #search {
        font-size: 1.2em;
    }
</style>

<script type="text/javascript">

</script>

























