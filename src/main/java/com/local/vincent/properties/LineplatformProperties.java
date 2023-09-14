package com.local.vincent.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "info")
public class LineplatformProperties {

    private String accessToken;
    private String secret;
    private String vincentUserId;
	private String url;
	private String passwordBase;
    private String liffUrl;
    
	public void accessInf(String accessToken, String secret, String vincentUserId, String url, String passwordBase, String liffUrl) {
		
		this.accessToken = accessToken;
		this.secret = secret;
		this.vincentUserId = vincentUserId;
		this.url = url;
		this.passwordBase = passwordBase;
		this.liffUrl = liffUrl;
	}
}
