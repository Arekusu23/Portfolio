package com.arekusu.playlist.models.dtos;

import com.arekusu.playlist.models.entities.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Valid
public class SavePlaylistDTO {
    
    @NotEmpty
    private String title;
    
    @NotEmpty
    private String description;
 
    @NotEmpty
    @Pattern(regexp = "^[0-9A-Z]{4}$")
    private User user;
}
