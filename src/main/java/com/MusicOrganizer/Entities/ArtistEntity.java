package com.MusicOrganizer.Entities;

import javax.persistence.*;

/**
 * Created by user on 1/24/2017.
 */
@Entity
@Table(name = "artist")
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_seq", nullable = false, updatable = false)
    private long id;

    @Column(name = "name", nullable = false, updatable = true)
    private String artist;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
