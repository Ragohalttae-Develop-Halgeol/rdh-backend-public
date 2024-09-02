package com.sfermions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfermions.entity.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);
}