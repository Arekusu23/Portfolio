package com.arekusu.playlist.services;

import com.arekusu.playlist.models.dtos.SongDTO;
import com.arekusu.playlist.models.entities.Song;

import java.util.List;

public interface SongService {
    List<Song> getAllSongs();
    
    List<Song> searchSongsByTitle(String title);
    boolean deleteSongByTitle(String title);
    Song createSong(Song song);
    Song createSong(SongDTO songDTO);
    Song searchSongByCode(String code);
}