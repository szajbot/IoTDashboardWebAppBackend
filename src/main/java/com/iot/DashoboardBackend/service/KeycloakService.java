package com.iot.DashoboardBackend.service;

import com.iot.DashoboardBackend.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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

    private Keycloak useToken(String token) {
        return KeycloakBuilder.builder()
                .serverUrl(server_url)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(client_id)
                .authorization(token)
                .clientSecret(client_secret)
                .build();
    }

    private Keycloak useSecret() {
        return KeycloakBuilder.builder()
                .serverUrl(server_url)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(client_id)
                .clientSecret(client_secret)
                .build();
    }

    public Response register(UserDto userDto) {
        var userCredentials = createPasswordRepresentation(userDto.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDto.getUsername());
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setFirstName(userDto.getFirstName());
        userRepresentation.setLastName(userDto.getLastName());
        userRepresentation.setCredentials(userCredentials);
        userRepresentation.setEnabled(true);
        Keycloak keycloak = useSecret();
        return keycloak.realm(realm).users().create(userRepresentation);
    }

    private List<CredentialRepresentation> createPasswordRepresentation(String password) {

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setValue(password);
        credentials.setType("password");
        credentials.setTemporary(false);

        var credentialList = new ArrayList<CredentialRepresentation>();
        credentialList.add(credentials);

        return credentialList;
    }

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

    public UserRepresentation getUser(String id, String accessToken) {
        Keycloak keycloak = useToken(accessToken);
        return keycloak.realm(realm).users().get(id).toRepresentation();
    }

    public UserRepresentation getUser(String id) {
        Keycloak keycloak = useSecret();
        return keycloak.realm(realm).users().get(id).toRepresentation();
    }

    public void edit(UserDto userDto, String userId, String accessToken) {
        Keycloak keycloak = useToken(accessToken);
        UserRepresentation user = this.getUser(userId, accessToken);
        user.setId(userId);
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        userResource.update(user);
    }

}
