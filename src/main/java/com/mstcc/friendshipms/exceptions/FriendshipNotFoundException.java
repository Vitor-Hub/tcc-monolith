package com.mstcc.friendshipms.exceptions;

public class FriendshipNotFoundException extends RuntimeException {
  public FriendshipNotFoundException(Long id) {
    super("Amizade com ID " + id + " não encontrada.");
  }
}