package com.arekusu.playlist.services.implement;

import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.Song;
import com.arekusu.playlist.models.entities.SongXPlaylist;
import com.arekusu.playlist.repositories.SongXPlaylistRepository;
import com.arekusu.playlist.services.SongXPlaylistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongXPlaylistServiceImplement implements SongXPlaylistService{
	
	private final SongXPlaylistRepository songXPlaylistRepository;
	
	@Autowired
	public SongXPlaylistServiceImplement(SongXPlaylistRepository songXPlaylistRepository) {
        this.songXPlaylistRepository = songXPlaylistRepository;
    }


	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(java.sql.Timestamp time, Song song, Playlist playlist) {
		
		SongXPlaylist existingSXP = songXPlaylistRepository.findOneSongXRepositoryBySongAndPlaylist(song, playlist);
        if (existingSXP != null) {
            throw new IllegalArgumentException("Esta cancion ya esta en la playlist.");
        }
        SongXPlaylist SXP = new SongXPlaylist(
        		time,
        		song,
        		playlist
        		);
        		songXPlaylistRepository.save(SXP);
	}

	@Override
	public List<SongXPlaylist> findAll() {
		return songXPlaylistRepository.findAll();
	}


	@Override
	public  List<SongXPlaylist> findByPlaylist(Playlist playlist) {
		
		return songXPlaylistRepository.findByPlaylist(playlist);
	}
	
	
}
