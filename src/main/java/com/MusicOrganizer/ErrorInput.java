package com.MusicOrganizer;

import com.MusicOrganizer.Entities.SongEntity;
import com.MusicOrganizer.Repositories.SongRepository;

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
    private SongRepository songRepository;
    private int id;

    public static final String CONSOLE = "console";
    public static final String PRINT = "print";

    //pass in song entity to test
//    public ErrorInput(SongEntity se, SongRepository songRepository, int id) {
//        songEntity = se;
//        rating = Integer.toString(se.getRating());
//        errorMessages = new ArrayList<>();
//        this.songRepository = songRepository;
//        this.id = id;
//        updateErrors();
//    }

    //pass in input values to create song entity with
    public ErrorInput(String title, String artist, String genre, String rating,
                      SongRepository songRepository, int id) {
        songEntity = new SongEntity(title, artist, genre, 0);
        this.rating = rating;
        errorMessages = new ArrayList<>();
        this.songRepository = songRepository;
        this.id = id;
        updateErrors();
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
        List<SongEntity> allSongs = songRepository.findByTitleIgnoreCase(songEntity.getTitle());
        boolean hasDuplicate = allSongs.size() > 0;
        return hasDuplicate;
    }
}
