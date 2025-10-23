package com.example.demo_api_movie.EpisodeService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findByMovieId(Long movieId);
    Optional<Episode> findByMovieIdAndNumber(Long movieId, Long number);
}
