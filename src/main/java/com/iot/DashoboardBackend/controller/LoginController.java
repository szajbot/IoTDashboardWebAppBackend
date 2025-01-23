package com.iot.DashoboardBackend.controller;

import com.iot.DashoboardBackend.model.dto.UserDto;
import com.iot.DashoboardBackend.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.security.Principal;

@CrossOrigin(maxAge = 3600)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {
    private final KeycloakService keycloakService;

    @PreAuthorize("isAnonymous()")
    @PostMapping(path = "/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDto userDto) {
        log.info("register({})", userDto);
        Response response = keycloakService.register(userDto);
        return ResponseEntity.status(response.getStatus()).body(response.getStatusInfo().toString());
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping(path = "/login")
    public ResponseEntity<AccessTokenResponse> login(String username, String password) {
        log.info("login({})", username);
        try {
            var accessToken = keycloakService.login(username, password);
            return ResponseEntity.ok().body(accessToken);
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(401).body(new AccessTokenResponse());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/edit")
    public ResponseEntity<String> edit(@RequestBody @Valid UserDto userDto, Principal principal, @RequestHeader("Authorization") String bearerToken) {
        log.info("edit({})", userDto);
        String accessToken = bearerToken.replace("Bearer ", "");
        keycloakService.edit(userDto, principal.getName(), accessToken);
        return ResponseEntity.status(200).body("Changed");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/logged")
    public ResponseEntity<Boolean> isLogged() {
        return ResponseEntity.status(200).body(false);
    }
}
