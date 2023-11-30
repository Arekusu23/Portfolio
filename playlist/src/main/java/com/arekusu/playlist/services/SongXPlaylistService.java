package com.arekusu.playlist.services;

import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.Song;
import com.arekusu.playlist.models.entities.SongXPlaylist;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface SongXPlaylistService {
    List<SongXPlaylist> findAll();
    
    void save(Timestamp timestamp, Song song, Playlist playlist);

	List<SongXPlaylist> findByPlaylist(Playlist playlist);
	
}
