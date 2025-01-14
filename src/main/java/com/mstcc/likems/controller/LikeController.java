package com.mstcc.likems.controller;

import com.mstcc.likems.dto.LikeDTO;
import com.mstcc.likems.services.LikeService;
import com.mstcc.likems.exceptions.LikeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // Endpoint para criar um like
    @PostMapping
    public ResponseEntity<LikeDTO> createLike(@RequestBody LikeDTO likeDto) {
        try {
            LikeDTO createdLike = likeService.createLike(likeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLike);
        } catch (LikeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para deletar um like
    @DeleteMapping("/{likeId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long likeId) {
        try {
            likeService.deleteLike(likeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (LikeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para obter um like específico pelo ID
    @GetMapping("/{likeId}")
    public ResponseEntity<LikeDTO> getLikeById(@PathVariable Long likeId) {
        try {
            LikeDTO like = likeService.getLikeById(likeId);
            return ResponseEntity.ok(like);
        } catch (LikeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
