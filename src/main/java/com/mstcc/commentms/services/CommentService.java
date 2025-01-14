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

    // Criação de um comentário
    public CommentDTO createComment(CommentDTO commentDto) {
        // Verifica se o post existe
        Optional<Post> post = postRepository.findById(commentDto.getPostId());
        if (post.isEmpty()) {
            throw new IllegalArgumentException("Post não encontrado ou inválido.");
        }

        // Verifica se o usuário existe
        Optional<User> user = userRepository.findById(commentDto.getUserId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado ou inválido.");
        }

        // Criação do comentário
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setPostId(post.get().getId());  // Define o ID do post no comentário
        comment.setUserId(user.get().getId());  // Define o ID do usuário no comentário

        // Salva o comentário no banco de dados
        Comment savedComment = commentRepository.save(comment);

        // Retorna o DTO do comentário criado
        return convertToDto(savedComment);
    }

    // Atualização de um comentário existente
    public CommentDTO updateComment(Long id, CommentDTO commentDto) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new CommentNotFoundException(id);
        }

        Comment comment = existingComment.get();
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);

        return convertToDto(comment);
    }

    // Deletando um comentário
    public void deleteComment(Long id) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new CommentNotFoundException(id);
        }

        commentRepository.deleteById(id);
    }

    // Obtendo todos os comentários
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtendo um comentário específico pelo ID
    public CommentDTO getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new CommentNotFoundException(id);
        }

        return convertToDto(comment.get());
    }

    // Método de conversão de Comment para CommentDTO
    private CommentDTO convertToDto(Comment comment) {
        // Busca o usuário associado ao comentário
        Optional<User> userOptional = userRepository.findById(comment.getUserId());
        UserDTO userDTO = userOptional.map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail())).orElse(null);

        // Busca o post associado ao comentário
        Optional<Post> postOptional = postRepository.findById(comment.getPostId());
        PostDTO postDTO = postOptional.map(p -> new PostDTO(p.getId(), p.getContent(), p.getCreatedAt(), userDTO, null)).orElse(null);

        // Retorna o DTO do comentário com o post e o usuário
        return new CommentDTO(comment.getId(), comment.getContent(), comment.getCreatedAt(), userDTO.getId(), postDTO.getId());
    }

    // Busca os comentários de um post específico
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}