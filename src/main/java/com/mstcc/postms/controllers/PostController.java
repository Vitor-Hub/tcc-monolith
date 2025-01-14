package com.mstcc.postms.controllers;

import com.mstcc.postms.dto.PostDTO;
import com.mstcc.postms.exceptions.PostNotFoundException;
import com.mstcc.postms.services.PostService;
import com.mstcc.userms.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Endpoint para criar um post
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDto) {
        PostDTO createdPost = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // Endpoint para atualizar um post
    @PutMapping("/user/{userId}/posts/{postId}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Long userId,
            @PathVariable Long postId,
            @RequestBody PostDTO postDto
    ) {
        try {
            PostDTO updatedPost = postService.updatePostForUser(userId, postId, postDto);
            return ResponseEntity.ok(updatedPost);
        } catch (PostNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para obter todos os posts
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Endpoint para obter um post específico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        try {
            PostDTO post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para deletar um post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para obter todos os posts de um usuário específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Long userId) {
        List<PostDTO> posts = postService.getPostsByUserId(userId);
        if (posts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(posts);
    }
}
