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

    private List<String> errorMessages;
    private SongRepository songRepo;
    private AlbumRepository albumRepo;
    private ArtistRepository artistRepo;

    public static final String CONSOLE = "console";
    public static final String PRINT = "print";


    public ErrorInput(SongRepository songRepo, AlbumRepository albumRepo, ArtistRepository artistRepo) {
        this.songRepo = songRepo;
        this.albumRepo = albumRepo;
        this.artistRepo = artistRepo;
    }

    public String getErrorStrings(String type) {
        String errmsgs = "";
        for(String errmsg: errorMessages)
            errmsgs += errmsg + "\n";
        return errmsgs;
    }

    public ArrayList<String> checkArtist(String artist) {
        ArrayList<String> emessages = new ArrayList<>();
        List<ArtistEntity> artists = artistRepo.findAll();
        for(ArtistEntity artistEntity: artists)
            if(artistEntity.getArtist().toLowerCase().equals(artist.toLowerCase()))
                emessages.add("Artist already exists");
        errorMessages = emessages;
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
        errorMessages = emessages;
        return emessages;
    }

    public ArrayList<String> checkSong(String song, String genre, String rating, String album, String date, String artist) {
        ArrayList<String> emessages = new ArrayList<>();
        List<SongEntity> songs = songRepo.findAll();
        for(SongEntity songEntity: songs){
            boolean songDupl = songEntity.getTitle().toLowerCase().equals(song.toLowerCase());
            boolean albumDupl = songEntity.getAlbumEntity().getTitle().toLowerCase().equals(album.toLowerCase());
            boolean artistDupl = songEntity.getAlbumEntity().getArtistEntity().getArtist().toLowerCase().equals(artist.toLowerCase());
            if(songDupl && albumDupl && artistDupl)
                emessages.add("Song already exists in " + album + " by " + artist);
        }
        if(song.equals(""))
            emessages.add("Song title cannot be empty");
//        if(genre.equals("") || genre == null)
//            emessages.add("Genre cannot be empty");
//        if(date.equals("") || date == null)
//            emessages.add("Date cannot be empty");
        int rat;
        try {
            rat = Integer.parseInt(rating);
            if(rat > 5 || rat < 1)
                emessages.add("Rating must be within range 1-5");
        } catch (NumberFormatException ex) {
            emessages.add("Rating must be an integer ranging from 1 to 5");
        }
        errorMessages = emessages;
        return emessages;
    }
}
