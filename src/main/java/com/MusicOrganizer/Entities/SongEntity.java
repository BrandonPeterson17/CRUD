package com.MusicOrganizer.Entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by user on 1/11/2017.
 */
@Entity
@Table(name = "song")
public class SongEntity {

    public SongEntity(String title, String genre, int rating) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    public SongEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_seq", nullable = false, insertable = false, updatable = true)
    private long id;
    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    @Column(name = "title", nullable = false, insertable = true, updatable = true)
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "genre", nullable = true, insertable = true, updatable = true)
    private String genre;
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Column(name = "rating", nullable = true, insertable = true, updatable = true)
    private int rating;
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Column(name = "album_seq")
    private long albumId;
    public long getAlbumId() {
        return albumId;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    @ManyToOne
    @JoinColumn(name = "album_seq", insertable = false, updatable = false)
    private AlbumEntity albumEntity;
    public AlbumEntity getAlbumEntity() {
        return albumEntity;
    }
    public void setAlbumEntity(AlbumEntity albumEntity) {
        this.albumEntity = albumEntity;
    }

    public ArtistEntity getArtistEntity() {
        return albumEntity.getArtistEntity();
    }

    public String getString() {
        String ofTheForce = title + " - " + albumEntity.getArtistEntity().getArtist()
                + " (" + albumEntity.getTitle()
                + " " + albumEntity.getDate() + ") " + genre +
                " rating: " + rating + " out of 5";
        return ofTheForce;
    }
}
