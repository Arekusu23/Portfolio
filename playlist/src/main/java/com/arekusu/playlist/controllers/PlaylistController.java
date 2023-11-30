package com.arekusu.playlist.controllers;

import com.arekusu.playlist.models.dtos.MessageDTO;
import com.arekusu.playlist.models.dtos.PlaylistDTO;
import com.arekusu.playlist.models.dtos.SavePlaylistDTO;
import com.arekusu.playlist.models.dtos.SongxPlaylistDTO;
import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.Song;
import com.arekusu.playlist.models.entities.SongXPlaylist;
import com.arekusu.playlist.models.entities.User;
import com.arekusu.playlist.services.PlaylistService;
import com.arekusu.playlist.services.SongService;
import com.arekusu.playlist.services.SongXPlaylistService;
import com.arekusu.playlist.services.UserService;
import com.arekusu.playlist.utils.RequestErrorHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class PlaylistController {
	
	@Autowired
    private final PlaylistService playlistService;
	@Autowired
    private final SongService songService;
	@Autowired
    private final UserService userService;
	
	@Autowired
    private final SongXPlaylistService songXPlaylistService;
	@Autowired
	private RequestErrorHandler errorHandler;
	
    @Autowired
    public PlaylistController(PlaylistService playlistService, SongService songService, UserService userService, SongXPlaylistService songXPlaylistService) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.userService = userService;
        this.songXPlaylistService = songXPlaylistService;
    }

    @PostMapping("/playlist")
    public ResponseEntity<?> savePlaylist(@RequestBody @Valid SavePlaylistDTO playlistDTO, BindingResult validations) {
    	
    	if(validations.hasErrors()) {
    		return new ResponseEntity<> (
        			errorHandler.mapErrors(validations.getFieldErrors()),HttpStatus.BAD_REQUEST);
    	}  
    	
        User user = userService.getUserByUsername(playlistDTO.getUser().getUsername(),playlistDTO.getUser().getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        try {
            playlistService.save(playlistDTO, user);
            return ResponseEntity.ok("Playlist guardada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al guardar la playlist");
        }
    }

    @DeleteMapping("/deleteByTitle/{title}")
    public ResponseEntity<String> deleteByTitle(@PathVariable String title) {
        try {
            playlistService.deleteByTitle(title);
            return ResponseEntity.ok("Playlist eliminada con éxito");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al eliminar la playlist");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Playlist>> findAllPlaylist() {
        List<Playlist> playlists = playlistService.findAll();

        if (playlists == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(playlists);
    }
    
    @PostMapping("/playlist/{playlistCode}")
    public ResponseEntity<?> addsSongToPlaylist(@PathVariable String playlistCode,@ModelAttribute @Valid SongxPlaylistDTO data , BindingResult validations) {
        if (validations.hasErrors()) {
            return new ResponseEntity<>(validations.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        
        Song song = songService.searchSongByCode(data.getSongCode());
        
        List<SongXPlaylist> songXplaylists = songXPlaylistService.findAll();
        boolean flag = true;
        
        for (int i = 0; i < songXplaylists.size(); i++) {
            if (songXplaylists.get(i).getSong().equals(song)) {
                flag = false;
                break;
            }
        }
        
        if (!flag) {
            return new ResponseEntity<>(new MessageDTO("Song Duplicate"), HttpStatus.BAD_REQUEST);
        }
        
        Playlist playlist = playlistService.findOneById(playlistCode);
        
        if (song != null && playlist != null) {
            try {
                songXPlaylistService.save(new Timestamp(System.currentTimeMillis()), song, playlist);
                return new ResponseEntity<>(new MessageDTO("¡Canción añadida a la playlist!"), HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        
        return new ResponseEntity<>(new MessageDTO("No se encontró la canción o la playlist"), HttpStatus.NOT_FOUND);
    }
    
    
    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<?> getPlaylistDuration(@PathVariable String playlistId) {

        Playlist playlist = playlistService.findOneById(playlistId);
        if (playlist == null) {
            return ResponseEntity.notFound().build();
        }

        List<SongXPlaylist> songXPlaylists = songXPlaylistService.findByPlaylist(playlist);
        int totalDurationInSeconds = songXPlaylists.stream()
                .mapToInt(songXPlaylist -> songXPlaylist.getSong().getDuration())
                .sum();

        int minutes = totalDurationInSeconds / 60;  // Obtiene los minutos
        int seconds = totalDurationInSeconds % 60;  // Obtiene los segundos

        List<Song> songs = songXPlaylists.stream().map(SongXPlaylist::getSong).collect(Collectors.toList());
        System.out.println(songs);

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setCode(playlist.getCode());
        playlistDTO.setTitle(playlist.getTitle());
        playlistDTO.setDescription(playlist.getDescription());
        playlistDTO.setSongs(songs);
        playlistDTO.setTotalDurationMinutes(minutes);
        playlistDTO.setTotalDurationSeconds(seconds);

        return ResponseEntity.ok(playlistDTO);
    }
    
}	
   
