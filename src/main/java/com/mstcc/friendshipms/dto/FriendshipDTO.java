package com.mstcc.friendshipms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FriendshipDTO {
    private Long id;
    private Long userId1;
    private Long userId2;
    private String status;
    private LocalDateTime createdAt;
}