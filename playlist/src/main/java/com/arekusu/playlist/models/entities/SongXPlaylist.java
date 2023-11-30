package com.arekusu.playlist.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "songxplaylist")
public class SongXPlaylist {

	@Id
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "song_code", nullable = true)
	private Song song;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "playlist_code", nullable = true)
	private Playlist playlist;
	
	@Column(name = "date_added")
	@NotEmpty
	private Timestamp dateAdded;	

	public SongXPlaylist(@NotEmpty Timestamp dateAdded, Song song, Playlist playlist) {
		super();
		this.dateAdded = dateAdded;
		this.song = song;
		this.playlist = playlist;
	}
	
	
	
}
