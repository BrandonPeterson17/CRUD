package com.MusicOrganizer.Repositories;

import com.MusicOrganizer.Entities.AlbumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by user on 1/24/2017.
 */
@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    Page<AlbumEntity> findAll(Pageable pageable);

    AlbumEntity findByTitleIgnoreCase(String title);

    AlbumEntity findById(long id);

//    @Query("SELECT a FROM AlbumEntity a LEFT JOIN FETCH a.songEntities WHERE a.songEntities.id=:songId")
//    List<AlbumEntity> findById(@Param("songId") String aongId);

}
