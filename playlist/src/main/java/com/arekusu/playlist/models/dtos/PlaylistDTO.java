package com.arekusu.playlist.models.dtos;

import com.arekusu.playlist.models.entities.Song;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
	private UUID code;
	private String title;
	private String description;
	private List<Song> songs;
	@JsonIgnore
	private int totalDuration;
	@JsonIgnore
	private int totalDurationMinutes;
	@JsonIgnore
	private int totalDurationSeconds;
	
	
	public String getPlaylistDuration() {
        return String.format("%02d:%02d", totalDurationMinutes, totalDurationSeconds);
    }

}
