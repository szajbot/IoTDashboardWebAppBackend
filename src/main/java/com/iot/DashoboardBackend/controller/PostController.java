package com.iot.DashoboardBackend.controller;

import com.iot.DashoboardBackend.model.dto.PostDto;
import com.iot.DashoboardBackend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authorization.client.util.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(maxAge = 3600)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(Principal principal, @RequestHeader("Authorization") String bearerToken) {
        log.info("getPosts({},{})", principal, bearerToken);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAll());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostDto> getPostById(Principal principal, @RequestHeader("Authorization") String bearerToken, @PathVariable("postId") int postId) {
        log.info("getPostById ({},{})", principal, bearerToken);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(Long.valueOf(postId)));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<String> addPost(@RequestBody @Valid PostDto postDto, Principal principal) {
        log.info("addPost({},{})", postDto, principal.getName());
        postService.create(postDto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}