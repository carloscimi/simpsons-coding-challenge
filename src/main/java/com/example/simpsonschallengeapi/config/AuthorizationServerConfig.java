package com.example.simpsonschallengeapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${jwt.secret}")
	private String secret;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("Virtusa")
				.secret("$2a$10$9QlG.22ddX58/NB1RO3aDu7B..AlwfnePoMAT7cfVVsrbv2Hq5Yza") //virtus@
				.scopes("read", "write")
				.authorizedGrantTypes("password")
				.accessTokenValiditySeconds(1800);
	}
	
	@Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())
			.authenticationManager(authenticationManager);
    }
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey(secret);
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}
