package com.example.demo_api_movie.RatingService;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserId(Long userId);
    Boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);
    List<Rating> findAllByUserId(Long userId);
}
