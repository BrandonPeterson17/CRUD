package com.MusicOrganizer.Entities;

import javax.persistence.*;
import java.util.Set;

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

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artistEntity", cascade = CascadeType.ALL)
//    Set<AlbumEntity> albumEntities;
//    public Set<AlbumEntity> getAlbumEntities() {
//        return albumEntities;
//    }
//    public void setAlbumEntities(Set<AlbumEntity> albumEntities) {
//        this.albumEntities = albumEntities;
//    }
}
