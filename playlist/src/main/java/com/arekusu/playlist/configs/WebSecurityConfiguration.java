package com.arekusu.playlist.configs;

import com.arekusu.playlist.models.entities.User;
import com.arekusu.playlist.services.UserService;
import com.arekusu.playlist.utils.JWTTokenFIlter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTTokenFIlter filter;
	
	@Bean
	AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
	    AuthenticationManagerBuilder managerBuilder 
	    	= http.getSharedObject(AuthenticationManagerBuilder.class);
	    
	    managerBuilder
	    	.userDetailsService(identifier -> {
	    		User user = userService.findOneByIdentifier(identifier, identifier);
	    		
	    		if(user == null)
	    			throw new UsernameNotFoundException("User: " + identifier + ", not found!");
	    		
	    		return user;
	    	})
	    	.passwordEncoder(passwordEncoder);
	    
	    return managerBuilder.build();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic().and().csrf().disable();
		
		 //Route filter
	    http.authorizeHttpRequests(auth -> 
	    	auth
	    		.requestMatchers("/auth/**").permitAll()
	    		.anyRequest().authenticated()
	    );
	    
	    //Statelessness
	    http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    
	    //UnAunthorized handler
	    http.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex) -> {
	        res.sendError(
	        		HttpServletResponse.SC_UNAUTHORIZED,
	        		"Auth fail!"
	        	);
	    }));
	    
	    //JWT filter
	    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}