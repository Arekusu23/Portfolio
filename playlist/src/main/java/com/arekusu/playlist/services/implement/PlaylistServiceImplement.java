package com.arekusu.playlist.services.implement;

import com.arekusu.playlist.models.dtos.SavePlaylistDTO;
import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.User;
import com.arekusu.playlist.repositories.PlaylistRepository;
import com.arekusu.playlist.services.PlaylistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlaylistServiceImplement implements PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistServiceImplement(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(SavePlaylistDTO info, User user) throws Exception {
        Playlist playlist = new Playlist();
        playlist.setTitle(info.getTitle());
        playlist.setDescription(info.getDescription());
        playlist.setUser(user); 
        playlistRepository.save(playlist);
    }


    @Override
    public void deleteByTitle(String title) throws Exception {
        playlistRepository.deleteByTitle(title);
    }
    @Override
    public Playlist findOneById(String id) {
        
        try {
        	UUID playlistId = UUID.fromString(id);
            return playlistRepository.findById(playlistId)
                    .orElse(null);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El ID de la playlist no es válido");
        }

    }


    @Override
    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }

	@Override
	public List<Playlist> findPlaylistsByUserAndTitle(User user, String title) {
		return playlistRepository.findByUserAndTitleContaining(user, title);
	}

	@Override
	public List<Playlist> findPlaylistByUser(User user) {
		   return playlistRepository.findByUser(user);
	}


	}



