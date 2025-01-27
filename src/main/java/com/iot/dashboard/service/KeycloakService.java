package com.iot.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.resource}")
    private String client_id;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String server_url;

    @Value("${keycloak.credentials.secret}")
    private String client_secret;

    public AccessTokenResponse login(String username, String password) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(server_url)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(client_id)
                .clientSecret(client_secret)
                .build();
        return keycloak.tokenManager().getAccessToken();
    }
}
