package com.arekusu.playlist.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "code")
	private UUID code;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "timestamp", insertable = false, updatable = false)
	private Date timestamp;
	
	@Column(name = "active", insertable = false)
	private Boolean active;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_code")
	@JsonIgnore
	private User user;

	public Token(String content, User user) {
		super();
		this.content = content;
		this.user = user;
	}
}