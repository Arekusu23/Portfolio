package com.arekusu.playlist.repositories;

import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.Song;
import com.arekusu.playlist.models.entities.SongXPlaylist;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SongXPlaylistRepository extends ListCrudRepository<SongXPlaylist, UUID> {
	SongXPlaylist findOneSongXRepositoryBySongAndPlaylist(Song song, Playlist playlist);
	
	 List<SongXPlaylist> findByPlaylist(Playlist playlist);
}
