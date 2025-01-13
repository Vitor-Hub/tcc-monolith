package com.mstcc.likems.repositories;

import com.mstcc.likems.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByPostId(Long postId);

    List<Like> findByCommentId(Long commentId);

    List<Like> findByUserId(Long userId);
}

