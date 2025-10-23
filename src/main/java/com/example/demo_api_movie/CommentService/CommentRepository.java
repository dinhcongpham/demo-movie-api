package com.example.demo_api_movie.CommentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsById(Long id);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.attachments WHERE c.movieId = :movieId ORDER BY c.createdAt DESC")
    Page<Comment> findByMovieIdOrderByCreatedAtDesc(@Param("movieId") Long movieId, Pageable pageable);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.attachments WHERE c.id = :commentId")
    Optional<Comment> getCommentByID(@Param("commentId") Long id);
}
