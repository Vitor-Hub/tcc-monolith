package com.mstcc.userms.services;

import com.mstcc.userms.entities.User;
import com.mstcc.userms.repositories.UserRepository;
import com.mstcc.userms.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usuário.
     *
     * @param user O usuário a ser criado.
     * @return O usuário criado.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Atualiza um usuário existente.
     *
     * @param id O ID do usuário a ser atualizado.
     * @param userDetails Os detalhes a serem atualizados.
     * @return O usuário atualizado.
     */
    public User updateUser(Long id, User userDetails) {
        // Busca o usuário existente
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)); // Lança exceção caso não encontrado

        // Atualiza os dados do usuário
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(userDetails.getPassword()); // Considere criptografar a senha
        return userRepository.save(existingUser);
    }

    /**
     * Deleta um usuário.
     *
     * @param id O ID do usuário a ser deletado.
     */
    public void deleteUser(Long id) {
        // Verifica se o usuário existe
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
    }

    /**
     * Retorna todos os usuários.
     *
     * @return Lista de todos os usuários.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retorna um usuário pelo seu ID.
     *
     * @param id O ID do usuário a ser buscado.
     * @return O usuário encontrado.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)); // Lança exceção caso não encontrado
    }
}
