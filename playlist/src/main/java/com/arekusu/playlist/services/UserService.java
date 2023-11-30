package com.arekusu.playlist.services;

import com.arekusu.playlist.models.dtos.UserDTO;
import com.arekusu.playlist.models.entities.Token;
import com.arekusu.playlist.models.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();
    User getUserByUsername(String username, String email);
    User createUser(UserDTO userDTO);
    User findOneByIdentifier(String username, String email);
    Boolean comparePassword(String toCompare, String current);
    Token registerToken(User user) throws Exception;
	Boolean isTokenValid(User user, String token);
	void cleanTokens(User user) throws Exception;
	User findUserAuthenticated();
}
