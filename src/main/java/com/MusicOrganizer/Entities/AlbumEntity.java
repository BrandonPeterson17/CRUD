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

    @Column(name = "title", nullable = false, insertable = true, updatable = true)
    private String title;

    @Column(name = "date", nullable = true, insertable = true, updatable = true)
    private String date;

    @OneToMany
    private Set<SongEntity> songEntities;

    public void setSongEntities(Set<SongEntity> songEntities) {
        this.songEntities = songEntities;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<SongEntity> getSongEntities() {
        return songEntities;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

}
