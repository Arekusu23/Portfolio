package com.arekusu.playlist.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Table(name="Playlist")
@ToString(exclude = {"songxplaylist"})
@NoArgsConstructor
@Data
@Entity
public class Playlist {
	
	@Id
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@Column(name = "title")
	@NotEmpty
	@Size(min = 5, max = 15)
	private String title;
	
	@Column(name = "description")
	@NotEmpty
	@Size(min = 10, max = 100)
	private String description;
	
	  @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "user_code", nullable=true)
	    private User user;
	
	@OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY)
	@JsonIgnore
    private List<SongXPlaylist> songxplaylist;

	public Playlist(@NotEmpty @Size(min = 5, max = 15) String title,
			@NotEmpty @Size(min = 10, max = 100) String description, User user) {
		super();
		this.title = title;
		this.description = description;
		this.user =user;
	}
}