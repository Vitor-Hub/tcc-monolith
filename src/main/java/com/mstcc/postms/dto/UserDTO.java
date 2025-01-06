package com.mstcc.postms.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    // Getters e Setters
    private Long id;
    private String username;
    private String email;

    // Construtor
    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}
