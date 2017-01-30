package com.MusicOrganizer.Entities;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by user on 1/24/2017.
 */
@Entity
@Table(name = "album")
public class AlbumEntity {

    public AlbumEntity(String title, String releaseDate) {
        this.title = title;
        this.date = releaseDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_seq", nullable = false, insertable = false, updatable = true)
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false, insertable = true, updatable = true)
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "date", nullable = true, insertable = true, updatable = true)
    private String date;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @OneToMany(mappedBy = "albumEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SongEntity> songEntities;
    public Set<SongEntity> getSongEntities() {
        return songEntities;
    }
    public void setSongEntities(Set<SongEntity> songEntities) {
        this.songEntities = songEntities;
    }

    @ManyToOne
    @JoinColumn(name = "artist_seq")
    private ArtistEntity artistEntity;
    public ArtistEntity getArtistEntity() {
        return artistEntity;
    }
    public void setArtistEntity(ArtistEntity artistEntity) {
        this.artistEntity = artistEntity;
    }

}
