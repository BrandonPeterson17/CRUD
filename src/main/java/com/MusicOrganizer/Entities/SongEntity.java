package com.MusicOrganizer.Entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by user on 1/11/2017.
 */
@Entity
@Table(name = "song")
public class SongEntity {

    public SongEntity(String title, String artist, String genre, int rating) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.rating = rating;
    }

    public SongEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_seq", nullable = false, insertable = false, updatable = true)
    private long id;

    @Column(name = "title", nullable = false, insertable = true, updatable = true)
    private String title;

    @Column(name = "genre", nullable = true, insertable = true, updatable = true)
    private String genre;

    @Column(name = "rating", nullable = true, insertable = true, updatable = true)
    private int rating;

    @Column(name = "artist", nullable = true, insertable = true, updatable = true)
    private String artist;

    public String getString() {
        String ofTheForce = title + " by " + artist + " - " + genre +
                " rating: " + rating + " out of 5";
        return ofTheForce;
    }

    public void setAll(String title, String artist, String genre, int rating, long albumKey) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }


}
