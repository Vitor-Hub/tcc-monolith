package com.mstcc.likems.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LikeDTO {
    private Long id;
    private Long userId;
    private Long postId;
    private Long commentId;
    private LocalDateTime createdAt;
}
