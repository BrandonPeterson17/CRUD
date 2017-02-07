package com.MusicOrganizer.Repositories;

import com.MusicOrganizer.Entities.AlbumEntity;
import com.MusicOrganizer.Entities.SongEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by user on 1/11/2017.
 */
@Repository
public interface SongRepository extends JpaRepository<SongEntity, Long> {

    Page<SongEntity> findAll(Pageable pageable);

    List<SongEntity> findByTitleIgnoreCase(String title);

    SongEntity findById(long id);

    @Query("SELECT s FROM SongEntity s WHERE LOWER(s.title) LIKE LOWER(:title)")
    Page<SongEntity> findByTitle(Pageable pageable, @Param("title") String title);

//    @Query("SELECT a FROM AlbumEntity a LEFT JOIN FETCH a.songEntities WHERE LOWER(SongEntity.title) LIKE LOWER(:title)")
//    List<AlbumEntity> findByTitle(@Param("title") String title);

}

