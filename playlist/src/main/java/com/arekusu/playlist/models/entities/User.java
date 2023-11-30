package com.arekusu.playlist.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@ToString(exclude = {"tokens"})
@Entity
@Table(name = "user")
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1460435087476558985L;
	
	@Column(name = "code")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@Column(name = "username")
	@NotEmpty
	@Size(min = 4, max = 15)
	private String username;
	
	
	@Column(name = "email")
	@Email
	private String email;
	
	@Column(name = "password")
	@NotEmpty
	@JsonIgnore
	@Pattern(regexp = "^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$")
	private String password;
	
	@Column(name = "active", insertable = false)
	private Boolean active;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Token> tokens;

	public User(@NotEmpty @Size(min = 4, max = 15) String username, @Email String email,
			@NotEmpty @Size(min = 8, max = 16) String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}


	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.active;
	}
}
