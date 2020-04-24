package com.example.simpsonschallengeapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


@Profile("oauth-security")
@Configuration
@EnableResourceServer
@EnableWebSecurity
public class ResourceSeverConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        	.withUser("admin").password("$2a$10$x1NogMEaBL8OZvFh3aUEOO7AIML7LYBrUksOADCi5wDPYZ6qyptRK").roles("ROLE");
    }
    
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated().and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.csrf().disable();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}
}
