package com.arekusu.playlist.services;

import com.arekusu.playlist.models.dtos.SavePlaylistDTO;
import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaylistService {
	void save (SavePlaylistDTO playlist , User user) throws Exception;
	void deleteByTitle(String title) throws Exception;
	Playlist findOneById(String id);
	List<Playlist> findAll();
	List<Playlist> findPlaylistByUser(User user);
    List<Playlist> findPlaylistsByUserAndTitle(User user, String title);
	
}