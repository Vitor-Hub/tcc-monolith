package com.mstcc.friendshipms.services;

import com.mstcc.friendshipms.entities.Friendship;
import com.mstcc.friendshipms.dto.FriendshipDTO;
import com.mstcc.friendshipms.exceptions.FriendshipNotFoundException;
import com.mstcc.friendshipms.repositories.FriendshipRepository;
import com.mstcc.userms.entities.User;
import com.mstcc.userms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    // Criar amizade
    public FriendshipDTO createFriendship(FriendshipDTO friendshipDto) {
        // Verifica se os dois usuários existem
        Optional<User> user1 = userRepository.findById(friendshipDto.getUserId1());
        Optional<User> user2 = userRepository.findById(friendshipDto.getUserId2());

        if (user1.isEmpty() || user2.isEmpty()) {
            throw new FriendshipNotFoundException(null);  // Lança exceção se um dos usuários não existir
        }

        Friendship friendship = new Friendship();
        friendship.setUserId1(friendshipDto.getUserId1());
        friendship.setUserId2(friendshipDto.getUserId2());
        friendship.setStatus(friendshipDto.getStatus());

        Friendship savedFriendship = friendshipRepository.save(friendship);
        return convertToDto(savedFriendship);
    }

    // Atualizar amizade
    public FriendshipDTO updateFriendship(Long id, FriendshipDTO friendshipDto) {
        Optional<Friendship> existingFriendship = friendshipRepository.findById(id);
        if (existingFriendship.isEmpty()) {
            throw new FriendshipNotFoundException(id);  // Lança exceção se amizade não existir
        }

        Friendship friendship = existingFriendship.get();
        friendship.setStatus(friendshipDto.getStatus());
        friendshipRepository.save(friendship);

        return convertToDto(friendship);
    }

    // Deletar amizade
    public void deleteFriendship(Long id) {
        Optional<Friendship> existingFriendship = friendshipRepository.findById(id);
        if (existingFriendship.isEmpty()) {
            throw new FriendshipNotFoundException(id);  // Lança exceção se amizade não existir
        }

        friendshipRepository.deleteById(id);
    }

    // Buscar amizade por ID
    public FriendshipDTO getFriendshipById(Long id) {
        Optional<Friendship> friendship = friendshipRepository.findById(id);
        if (friendship.isEmpty()) {
            throw new FriendshipNotFoundException(id);  // Lança exceção se amizade não existir
        }

        return convertToDto(friendship.get());
    }

    // Buscar todas as amizades
    public List<FriendshipDTO> getAllFriendships() {
        return friendshipRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Conversão de Friendship para FriendshipDTO
    private FriendshipDTO convertToDto(Friendship friendship) {
        return new FriendshipDTO(
                friendship.getId(),
                friendship.getUserId1(),
                friendship.getUserId2(),
                friendship.getStatus(),
                friendship.getCreatedAt()
        );
    }
}
