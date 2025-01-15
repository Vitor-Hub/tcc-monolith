package com.mstcc.friendshipms.controller;

import com.mstcc.friendshipms.dto.FriendshipDTO;
import com.mstcc.friendshipms.services.FriendshipService;
import com.mstcc.friendshipms.exceptions.FriendshipNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping
    public ResponseEntity<FriendshipDTO> createFriendship(@RequestBody FriendshipDTO friendshipDto) {
        try {
            FriendshipDTO createdFriendship = friendshipService.createFriendship(friendshipDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFriendship);
        } catch (FriendshipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Caso a amizade não possa ser criada
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FriendshipDTO> updateFriendship(@PathVariable Long id, @RequestBody FriendshipDTO friendshipDto) {
        try {
            FriendshipDTO updatedFriendship = friendshipService.updateFriendship(id, friendshipDto);
            return ResponseEntity.ok(updatedFriendship);
        } catch (FriendshipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Caso a amizade não seja encontrada
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long id) {
        try {
            friendshipService.deleteFriendship(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (FriendshipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FriendshipDTO>> getAllFriendships() {
        return ResponseEntity.ok(friendshipService.getAllFriendships());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FriendshipDTO> getFriendshipById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(friendshipService.getFriendshipById(id));
        } catch (FriendshipNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Caso a amizade não seja encontrada
        }
    }
}
