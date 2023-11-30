package com.arekusu.playlist.repositories;

import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends ListCrudRepository<Playlist, UUID> {
    List<Playlist> findByTitle(String title);
    List<Playlist> findByUser(User user);
    List<Playlist> findByUserAndTitleContaining(User user, String titleFragment);
    void deleteByTitle(String title);;
	
}