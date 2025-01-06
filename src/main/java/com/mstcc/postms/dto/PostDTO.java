package com.mstcc.postms.dto;

import com.mstcc.postms.entities.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostDTO {
    // Getters e Setters
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO user;
    private List<CommentDTO> comments;

    // Construtores, Getters e Setters
    public PostDTO(Long id, String content, LocalDateTime createdAt, UserDTO user, List<CommentDTO> comments) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        this.comments = comments;
    }

}
