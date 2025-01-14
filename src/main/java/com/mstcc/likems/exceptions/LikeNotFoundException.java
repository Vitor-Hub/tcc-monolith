package com.mstcc.likems.exceptions;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException(Long id) {
        super("Like com ID " + id + " não encontrado.");
    }

    public LikeNotFoundException(String msg) {
        super(msg);
    }
}