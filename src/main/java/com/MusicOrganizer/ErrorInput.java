package com.MusicOrganizer;

import com.MusicOrganizer.Entities.AlbumEntity;
import com.MusicOrganizer.Entities.ArtistEntity;
import com.MusicOrganizer.Entities.SongEntity;
import com.MusicOrganizer.Repositories.AlbumRepository;
import com.MusicOrganizer.Repositories.ArtistRepository;
import com.MusicOrganizer.Repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1/19/2017.
 */
public class ErrorInput {

    private SongEntity songEntity;
    private String rating;
    private List<String> errorMessages;
    private boolean hasErrors;
    private int id;
    private SongRepository songRepo;
    private AlbumRepository albumRepo;
    private ArtistRepository artistRepo;

    public static final String CONSOLE = "console";
    public static final String PRINT = "print";

    //pass in song entity to test
//    public ErrorInput(SongEntity se, SongRepository songRepo, int id) {
//        songEntity = se;
//        rating = Integer.toString(se.getRating());
//        errorMessages = new ArrayList<>();
//        this.songRepo = songRepo;
//        this.id = id;
//        updateErrors();
//    }

    //pass in input values to create song entity with
    public ErrorInput(String title, String artist, String genre, String rating,
                      SongRepository songRepository, int id) {
        songEntity = new SongEntity(title, genre, 0);
        this.rating = rating;
        errorMessages = new ArrayList<>();
        this.songRepo = songRepository;
        this.id = id;
        updateErrors();
    }

    public ErrorInput(SongRepository songRepo, AlbumRepository albumRepo, ArtistRepository artistRepo) {
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.artistRepo = artistRepo;
    }

    public SongEntity getSongEntity() {
        return songEntity;
    }

    public void setSongEntity(SongEntity songEntity) {
        this.songEntity = songEntity;
        errorMessages = new ArrayList<>();
        updateErrors();
    }

    public List<String> getErrorMessages() {
        updateErrors();
        return errorMessages;
    }

    public String getErrorStrings(String type) {
        updateErrors();
        String errmsgs = "";
        for(String errmsg: errorMessages)
            errmsgs += errmsg + "\n";
        return errmsgs;
    }

    public boolean hasErrors() {
        updateErrors();
        return hasErrors;
    }

    //finds all input errors in song entitiy
    private void updateErrors() {
        errorMessages = new ArrayList<>();
        if(songEntity.getTitle().equals(""))
            errorMessages.add("Title can't be left empty");
        if(existsDuplicateTitle())
            errorMessages.add("Title already exists");
        int tempRating;
        if(rating == null) {
            errorMessages.add("Rating can't be left empty");
        } else {
            try {
                tempRating = Integer.parseInt(rating);
                songEntity.setRating(tempRating);
                if(tempRating > 5 || tempRating < 1)
                    errorMessages.add("Rating must be within the range 1-5");
            } catch (NumberFormatException ex) {
                errorMessages.add("Rating must be a number within the range 1-5");
            }
        }
        hasErrors = errorMessages.size() > 0;
    }

    //searches repository for song with same title
    private boolean existsDuplicateTitle() {
        List<SongEntity> allSongs = songRepo.findByTitleIgnoreCase(songEntity.getTitle());
        boolean hasDuplicate = allSongs.size() > 0;
        return hasDuplicate;
    }

    public ArrayList<String> checkArtist(String artist) {
        ArrayList<String> emessages = new ArrayList<>();
        List<ArtistEntity> artists = artistRepo.findAll();
        for(ArtistEntity artistEntity: artists)
            if(artistEntity.getArtist().toLowerCase().equals(artist.toLowerCase()))
                emessages.add("Artist already exists");
        return emessages;
    }

    public ArrayList<String> checkAlbum(String album, String artist) {
        ArrayList<String> emessages = new ArrayList<>();
        List<AlbumEntity> albums = albumRepo.findAll();
        for(AlbumEntity albumEntity: albums) {
            boolean albumDupl = albumEntity.getTitle().toLowerCase().equals(album.toLowerCase());
            boolean artistDupl = albumEntity.getArtistEntity().getArtist().toLowerCase().equals(artist.toLowerCase());
            if (albumDupl && artistDupl)
                emessages.add("Album already exists by " + artist);
        }
        return emessages;
    }

    public ArrayList<String> checkSong(String song, String rating, String album, String artist) {
        ArrayList<String> emessages = new ArrayList<>();
        List<SongEntity> songs = songRepo.findAll();
        for(SongEntity songEntity: songs){
            boolean songDupl = songEntity.getTitle().toLowerCase().equals(song.toLowerCase());
            boolean albumDupl = songEntity.getAlbumEntity().getTitle().toLowerCase().equals(album.toLowerCase());
            boolean artistDupl = songEntity.getAlbumEntity().getArtistEntity().getArtist().toLowerCase().equals(artist.toLowerCase());
            if(songDupl && albumDupl && artistDupl)
                emessages.add("Song already exists in " + album + " by " + artist);
        }
        int rat;
        try {
            rat = Integer.parseInt(rating);
            if(rat > 5 || rat < 1)
                emessages.add("Rating must be within range 1-5");
        } catch (NumberFormatException ex) {
            emessages.add("Rating must be an integer ranging from 1 to 5");
        }
        return emessages;
    }
}
