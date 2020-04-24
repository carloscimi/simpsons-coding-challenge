package com.example.simpsonschallengeapi.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("SimpsonsCodingChallenge")
public class SimpsonsCodingChallengeApiProperty {

	private String allowedOrigin = "http://localhost:8083";

	private final Secutiry security = new Secutiry();

	public Secutiry getSecutiry() {
		return security;
	}

	public String getAllowedOrigin() {
		return allowedOrigin;
	}

	public void setAllowedOrigin(String allowedOrigin) {
		this.allowedOrigin = allowedOrigin;
	}

	public static class Secutiry {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	}
	
}
