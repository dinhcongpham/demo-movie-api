package com.example.demo_api_movie.MovieService;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
    Optional<Genre> findByName(String name);
    boolean existsByName(String name);
}
