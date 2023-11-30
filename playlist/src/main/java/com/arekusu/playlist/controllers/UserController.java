package com.arekusu.playlist.controllers;

import com.arekusu.playlist.models.dtos.MessageDTO;
import com.arekusu.playlist.models.dtos.TokenDTO;
import com.arekusu.playlist.models.dtos.UserDTO;
import com.arekusu.playlist.models.dtos.UserLoginDTO;
import com.arekusu.playlist.models.entities.Playlist;
import com.arekusu.playlist.models.entities.Token;
import com.arekusu.playlist.models.entities.User;
import com.arekusu.playlist.services.PlaylistService;
import com.arekusu.playlist.services.UserService;
import com.arekusu.playlist.utils.RequestErrorHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    
    @Autowired
    private PlaylistService playlistService;
 

    @Autowired
    public void PlaylistController(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }
    
    private RequestErrorHandler errorHandler;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username, username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO, BindingResult validations) {
    	
    	if(validations.hasErrors()) {
    		return new ResponseEntity<> (
        			errorHandler.mapErrors(validations.getFieldErrors()),HttpStatus.BAD_REQUEST);
    	}    	
        try {
            User createdUser = userService.createUser(userDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    
}
    
    @GetMapping("/user/playlist")
    public ResponseEntity<?> getPlaylistByUser(@RequestParam("User")String userData, @RequestParam(required = false) String title){
    	User user = userService.getUserByUsername(userData,title);
    	if(user == null) {
    		return new ResponseEntity<>(
    				new MessageDTO("user not found"),HttpStatus.NOT_FOUND);
    	}
    	List<Playlist> playlists;
        if (title != null && !title.isEmpty()) {
            playlists = playlistService.findPlaylistsByUserAndTitle(user, title);
        } else {
            playlists = playlistService.findPlaylistByUser(user);
        }
        return ResponseEntity.ok(playlists);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute @Valid UserLoginDTO info, BindingResult validations){
    	if(validations.hasErrors()) {
			return new ResponseEntity<>("Error inesperado", HttpStatus.BAD_REQUEST);
		}
		
		User user = userService.findOneByIdentifier(info.getIdentifier(), info.getIdentifier());
		
		if(user == null) {
			return new ResponseEntity<>("Usuario inexistente", HttpStatus.NOT_FOUND);
		}
		
		if(!userService.comparePassword(info.getPassword(), user.getPassword())) {
			return new ResponseEntity<>("Error de credenciales", HttpStatus.UNAUTHORIZED);
		}
		try {
			Token token = userService.registerToken(user);
			return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
 }  

