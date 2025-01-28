package com.iot.dashboard.service;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

    @Value("${keycloak.resource}")
    private String client_id;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String server_url;

    @Value("${keycloak.credentials.secret}")
    private String client_secret;

    private OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    public Response login(String username, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType,
                "grant_type=password" +
                        "&client_id=" + client_id +
                        "&client_secret=" + client_secret +
                        "&username=" + username +
                        "&password=" + password);

        Request request = new Request.Builder()
                .url(server_url + "/realms/" + realm + "/protocol/openid-connect/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public void refresh(String refresh_token) {
        //TODO to implementation
    }

    public void logout(String refresh_token) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType,
                "client_secret=" + client_secret +
                        "&client_id=" + client_id +
                        "&refresh_token=" + refresh_token);

        Request request = new Request.Builder()
                .url(server_url + "/realms/" + realm + "/protocol/openid-connect/logout")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = client.newCall(request).execute();
        log.info("logout");
    }
}
