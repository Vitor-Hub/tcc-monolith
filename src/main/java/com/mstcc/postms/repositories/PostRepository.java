package com.mstcc.postms.repositories;

import com.mstcc.postms.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    Optional<Post> findByIdAndUserId(Long id, Long userId);
}
