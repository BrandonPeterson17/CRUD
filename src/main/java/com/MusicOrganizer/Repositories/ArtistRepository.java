package com.MusicOrganizer.Repositories;

import com.MusicOrganizer.Entities.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by user on 1/31/2017.
 */
@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {

    List<ArtistEntity> findAll();

    Page<ArtistEntity> findAll(Pageable pageable);

    List<ArtistEntity> findById(long id);
}
