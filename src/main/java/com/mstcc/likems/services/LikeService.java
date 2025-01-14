package com.mstcc.likems.services;

import com.mstcc.likems.dto.LikeDTO;
import com.mstcc.likems.entities.Like;
import com.mstcc.likems.exceptions.LikeNotFoundException;
import com.mstcc.likems.repositories.LikeRepository;
import com.mstcc.postms.entities.Post;
import com.mstcc.postms.repositories.PostRepository;
import com.mstcc.commentms.entities.Comment;
import com.mstcc.commentms.repositories.CommentRepository;
import com.mstcc.userms.entities.User;
import com.mstcc.userms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // Criar um like
    public LikeDTO createLike(LikeDTO likeDto) {
        // Verificar se o usuário existe
        Optional<User> user = userRepository.findById(likeDto.getUserId());
        if (user.isEmpty()) {
            throw new LikeNotFoundException(likeDto.getUserId());
        }

        // Verificar se o post ou comentário existe
        Optional<Post> post = Optional.empty();
        Optional<Comment> comment = Optional.empty();
        if (likeDto.getPostId() != null) {
            post = postRepository.findById(likeDto.getPostId());
        } else if (likeDto.getCommentId() != null) {
            comment = commentRepository.findById(likeDto.getCommentId());
        }

        if (likeDto.getPostId() != null && post.isEmpty()) {
            throw new LikeNotFoundException("Post com ID " + likeDto.getPostId() + " não encontrado.");
        }

        if (likeDto.getCommentId() != null && comment.isEmpty()) {
            throw new LikeNotFoundException("Comentário com ID " + likeDto.getCommentId() + " não encontrado.");
        }

        // Criar o like
        Like like = new Like();
        like.setUserId(likeDto.getUserId());
        like.setPostId(likeDto.getPostId());
        like.setCommentId(likeDto.getCommentId());

        // Salvar o like
        Like savedLike = likeRepository.save(like);
        return convertToDto(savedLike);
    }

    // Deletar um like
    public void deleteLike(Long likeId) {
        Optional<Like> like = likeRepository.findById(likeId);
        if (like.isEmpty()) {
            throw new LikeNotFoundException(likeId);
        }
        likeRepository.deleteById(likeId);
    }

    // Conversão de Like para LikeDTO
    private LikeDTO convertToDto(Like like) {
        return new LikeDTO(like.getId(), like.getUserId(), like.getPostId(), like.getCommentId(), like.getCreatedAt());
    }

    // Obter um like por ID
    public LikeDTO getLikeById(Long likeId) {
        Optional<Like> like = likeRepository.findById(likeId);
        if (like.isEmpty()) {
            throw new LikeNotFoundException(likeId);
        }
        return convertToDto(like.get());
    }
}
