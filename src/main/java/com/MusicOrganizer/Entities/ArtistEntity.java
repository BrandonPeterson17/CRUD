package com.MusicOrganizer.Entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 1/24/2017.
 */
@Entity
@Table(name = "artist")
public class ArtistEntity {

    public ArtistEntity() {}

    public ArtistEntity(String artist) {
        this.artist = artist;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_seq", nullable = false, updatable = false)
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, updatable = true)
    private String artist;
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artistEntity", cascade = CascadeType.ALL)
    List<AlbumEntity> albumEntities;
    public List<AlbumEntity> getAlbumEntities() {
        return albumEntities;
    }
    public void setAlbumEntities(List<AlbumEntity> albumEntities) {
        this.albumEntities = albumEntities;
    }
}
