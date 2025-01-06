package com.mstcc.postms.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(Long postId) {
        super("Post with ID " + postId + " not found");
    }
}
