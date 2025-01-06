package com.mstcc.postms.services;

import com.mstcc.postms.dto.CommentDTO;
import com.mstcc.postms.dto.PostDTO;
import com.mstcc.postms.dto.UserDTO;
import com.mstcc.postms.entities.Post;
import com.mstcc.postms.repositories.PostRepository;
import com.mstcc.postms.repositories.UserRepository;
import com.mstcc.postms.repositories.CommentRepository;
import com.mstcc.postms.exceptions.PostNotFoundException;
import com.mstcc.postms.exceptions.UserNotFoundException;
import com.mstcc.userms.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // Método para criar um novo Post
    public PostDTO createPost(PostDTO postDto) {
        // Criar um novo Post a partir do DTO
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setUserId(postDto.getUser().getId());

        // Salvar no banco de dados
        Post savedPost = postRepository.save(post);

        // Converter o Post salvo para PostDTO e retornar
        return convertToPostDTOWithComments(savedPost);
    }

    // Método para atualizar um Post existente
    public PostDTO updatePost(Long id, PostDTO postDto) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException(id);
        }

        Post updatedPost = post.get();
        updatedPost.setContent(postDto.getContent());
        postRepository.save(updatedPost);

        return convertToPostDTOWithComments(updatedPost);
    }

    // Método para obter um Post pelo ID
    public PostDTO getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException(id);
        }
        return convertToPostDTOWithComments(post.get());
    }

    // Método para obter todos os Posts
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        // Converter cada Post para PostDTO e retornar
        return posts.stream()
                .map(this::convertToPostDTOWithComments)
                .collect(Collectors.toList());
    }

    // Método para deletar um Post
    public void deletePost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException(id);
        }

        postRepository.deleteById(id);
    }

    // Método para converter o Post em PostDTO, incluindo User e Comments
    private PostDTO convertToPostDTOWithComments(Post post) {
        // Buscar o usuário diretamente do banco
        Optional<User> user = userRepository.findById(post.getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException(post.getUserId());
        }

        // Converter User para UserDTO
        UserDTO userDTO = new UserDTO(user.get().getId(), user.get().getUsername(), user.get().getEmail());

        // Buscar os comentários diretamente do banco
        List<CommentDTO> comments = commentRepository.findByPostId(post.getId()).stream()
                .map(comment -> new CommentDTO(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUserId(), comment.getPostId()))
                .collect(Collectors.toList());

        // Retorna o PostDTO com o usuário e os comentários
        return new PostDTO(post.getId(), post.getContent(), post.getCreatedAt(), userDTO, comments);
    }
}
