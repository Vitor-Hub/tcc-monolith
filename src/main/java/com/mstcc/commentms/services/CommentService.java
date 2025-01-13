package com.mstcc.commentms.services;

import com.mstcc.commentms.dto.CommentDTO;
import com.mstcc.commentms.entities.Comment;
import com.mstcc.commentms.repositories.CommentRepository;
import com.mstcc.commentms.exceptions.CommentNotFoundException;
import com.mstcc.postms.dto.PostDTO;
import com.mstcc.postms.entities.Post;
import com.mstcc.postms.repositories.PostRepository;
import com.mstcc.userms.dto.UserDTO;
import com.mstcc.userms.entities.User;
import com.mstcc.userms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public CommentDTO createComment(CommentDTO commentDto) {
        // Criação do comentário
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPostId(commentDto.getPost().getId());
        comment.setUserId(commentDto.getUser().getId());
        Comment savedComment = commentRepository.save(comment);

        return convertToDto(savedComment);
    }

    public CommentDTO updateComment(Long id, CommentDTO commentDto) {
        // Atualização do comentário
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new CommentNotFoundException(id);  // Lançando exceção personalizada
        }

        Comment comment = existingComment.get();
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);

        return convertToDto(comment);
    }

    public void deleteComment(Long id) {
        // Deletando o comentário
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new CommentNotFoundException(id);  // Lançando exceção personalizada
        }

        commentRepository.deleteById(id);
    }

    public List<CommentDTO> getAllComments() {
        // Retorna todos os comentários
        return commentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommentDTO getCommentById(Long id) {
        // Buscando o comentário pelo ID
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException(id);  // Lançando exceção personalizada
        }

        return convertToDto(comment.get());
    }

    // Método de conversão de Comment para CommentDTO
    private CommentDTO convertToDto(Comment comment) {
        Optional<User> user = userRepository.findById(comment.getUserId());
        Optional<Post> post = postRepository.findById(comment.getPostId());

        UserDTO userDTO = user.map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail())).orElse(null);

        PostDTO postDTO = post.map(p -> new PostDTO(p.getId(), p.getContent(), p.getCreatedAt(), userDTO, null)).orElse(null);

        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                userDTO,
                postDTO
        );
    }
}
