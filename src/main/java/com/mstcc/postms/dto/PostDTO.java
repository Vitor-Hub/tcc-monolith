package com.mstcc.postms.dto;

import com.mstcc.commentms.dto.CommentDTO;
import com.mstcc.postms.entities.Post;
import com.mstcc.userms.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PostDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO user;
    private List<CommentDTO> comments;

}
