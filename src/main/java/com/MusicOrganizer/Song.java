package com.MusicOrganizer;

import com.MusicOrganizer.Entities.AlbumEntity;

import java.util.List;

/**
 * Created by user on 1/19/2017.
 */
public class Song {

    private String id;
    private String title;
    private String artist;
    private String genre;
    private String rating;
    private String date;
    private String album;
    private AlbumEntity albumEntity;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
