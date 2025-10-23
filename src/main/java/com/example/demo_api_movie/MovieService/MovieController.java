package com.example.demo_api_movie.MovieService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public MovieDto createMovie(@RequestBody MovieRequest request) {
        return movieService.createMovie(request);
    }

    @GetMapping
    public ResponseEntity<Page<?>> getMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<?> movies = movieService.getMovies(title, genreIds, director, country, year, page, size, "nope");
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/preview")
    public ResponseEntity<Page<?>> getMoviesPreview(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<?> movies = movieService.getMovies(title, genreIds, director, country, year, page, size, "preview");
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovieById(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public MovieDto updateMovieById(@PathVariable Long id, @RequestBody MovieRequest request) {
        return movieService.updateMovieById(id, request);
    }

    @GetMapping("/all")
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies();
    }
}

