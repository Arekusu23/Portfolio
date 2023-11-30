package com.arekusu.playlist.repositories;

import com.arekusu.playlist.models.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID> {
	List<Song> findByTitle(String title);
    List<Song> findByTitleContainingIgnoreCase(String title);
    Song findOneByCode(UUID code);
}
