package com.arekusu.playlist.models.dtos;

import com.arekusu.playlist.models.entities.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {
private String token;
	
	public TokenDTO(Token token) {
		this.token = token.getContent();
	}
}
