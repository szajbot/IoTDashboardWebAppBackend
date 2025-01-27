package com.iot.dashboard.controller;

import com.iot.dashboard.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {
    private final KeycloakService keycloakService;

    @PreAuthorize("isAnonymous()")
    @PostMapping(path = "/login")
    public ResponseEntity<AccessTokenResponse> login(String username, String password) {
        log.info("login({})", username);
        try {
            var accessToken = keycloakService.login(username, password);
            return ResponseEntity.ok().body(accessToken);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(new AccessTokenResponse());
        }
    }
}
