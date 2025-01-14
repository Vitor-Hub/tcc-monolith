package com.mstcc.postms.services;

import com.mstcc.commentms.dto.CommentDTO;
import com.mstcc.commentms.services.CommentService;  // Adicionando a dependência para CommentService
import com.mstcc.postms.dto.PostDTO;
import com.mstcc.postms.entities.Post;
import com.mstcc.postms.repositories.PostRepository;
import com.mstcc.postms.exceptions.PostNotFoundException;
import com.mstcc.userms.dto.UserDTO;
import com.mstcc.userms.entities.User;
import com.mstcc.userms.exceptions.UserNotFoundException;
import com.mstcc.userms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;  // Injeção do CommentService

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    // Método para criar um novo Post
    public PostDTO createPost(PostDTO postDto) {
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setUserId(postDto.getUser().getId());

        // Salvar no banco de dados
        Post savedPost = postRepository.save(post);

        return convertToPostDTOWithComments(savedPost);
    }

    // Método para atualizar um Post existente
    public PostDTO updatePostForUser(Long userId, Long postId, PostDTO postDto) {
        // Verificar se o usuário existe
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        // Buscar o post e verificar se pertence ao usuário
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new PostNotFoundException(postId);
        }

        Post post = postOptional.get();

        // Verifica se o post pertence ao usuário
        if (!post.getUserId().equals(userId)) {
            throw new PostNotFoundException(postId);  // Lança exceção caso o post não pertença ao usuário
        }

        // Atualiza o conteúdo do post
        post.setContent(postDto.getContent());
        postRepository.save(post);

        // Converte o post atualizado para DTO e retorna
        return convertToPostDTOWithComments(post);
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

    // Método de conversão de Post para PostDTO com comentários
    public PostDTO convertToPostDTOWithComments(Post post) {
        Optional<User> userOptional = userRepository.findById(post.getUserId());
        UserDTO userDTO = userOptional.map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail()))
                .orElse(null);

        List<CommentDTO> comments = commentService.getCommentsByPostId(post.getId());  // Buscar comentários associados ao post

        return new PostDTO(post.getId(), post.getContent(), post.getCreatedAt(), userDTO, comments);
    }

    // Busca os posts de um usuário com base no ID
    public List<PostDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(this::convertToPostDTOWithComments)  // Converte para PostDTO incluindo comentários
                .collect(Collectors.toList());
    }
}
