package com.mstcc.postms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDTO {
    // Getters e Setters
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private Long postId;

    // Construtor
    public CommentDTO(Long id, String content, LocalDateTime createdAt, Long userId, Long postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.postId = postId;
    }

}
