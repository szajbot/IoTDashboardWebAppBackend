package com.iot.dashboard.controller;

import com.iot.dashboard.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@CrossOrigin(maxAge = 3600)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {
    private final KeycloakService keycloakService;

    @PreAuthorize("isAnonymous()")
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(String username, String password) {
        log.info("login({})", username);
        log.info("pass({})", password);
        try {
            var response = keycloakService.login(username, password);
            return ResponseEntity.ok().body(Objects.requireNonNull(response.body()).string());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }

//    @PreAuthorize("permitAll()")
//    @PostMapping(path = "/refresh")
//    public ResponseEntity<AccessTokenResponse> refresh(String refreshToken) {
//        log.info("refresh");
//        try {
//            var accessToken = keycloakService.refresh(refreshToken);
//            return ResponseEntity.ok().body(accessToken);
//        } catch (Exception ex) {
//            log.info(ex.getMessage());
//            return ResponseEntity.status(400).body(new AccessTokenResponse());
//        }
//    }
//
    @PreAuthorize("permitAll()")
    @PostMapping(path = "/logout")
    public ResponseEntity<String> logout(String refreshToken) {
        log.info("refreshToken({})", refreshToken);
        try {
            keycloakService.logout(refreshToken);
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }

}
