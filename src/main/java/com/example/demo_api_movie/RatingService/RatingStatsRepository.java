package com.example.demo_api_movie.RatingService;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingStatsRepository extends JpaRepository<RatingStats, Long> {
    RatingStats findByMovieId(Long movieId);
    boolean existsByMovieId(Long movieId);
}
