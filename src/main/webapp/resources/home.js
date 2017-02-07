/**
 * Created by user on 2/2/2017.
 */
$(document).ready( function () {
    $('button').on('click', function () {
        $(this).text('Hello once more');
    });
});

function alrtMssg(artistId, songId) {
    alert('artist id: ' + artistId + '\nsong id: ' + songId);
}
