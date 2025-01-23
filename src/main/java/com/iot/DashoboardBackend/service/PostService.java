package com.iot.DashoboardBackend.service;

import com.iot.DashoboardBackend.model.Post;
import com.iot.DashoboardBackend.model.dto.PostDto;
import com.iot.DashoboardBackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final KeycloakService keycloakService;

    public List<PostDto> getAll() {
        var posts = postRepository.findAll();
        Collections.reverse(posts);

        List<PostDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            var user = keycloakService.getUser(post.getOwnerId());
            postDtos.add(new PostDto(
                    post.getId(),
                    user.getUsername(),
                    post.getTitle(),
                    post.getContent()
            ));
        }
        return postDtos;
    }

    public void create(PostDto postDto, String userId) {
        var post = Post.builder()
                .ownerId(userId)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();
        postRepository.save(post);
    }

    public PostDto getPostById(Long postId) {
        return postRepository.findById(postId)
                .map(post -> new PostDto(
                        post.getId(),
                        keycloakService.getUser(post.getOwnerId()).getUsername(),
                        post.getTitle(),
                        post.getContent()
                ))
                .orElseThrow(() -> new NoSuchElementException("Post with id not found!"));
    }
}