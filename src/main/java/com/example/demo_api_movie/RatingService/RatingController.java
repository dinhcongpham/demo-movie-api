package com.example.demo_api_movie.RatingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;
    private final RatingStatsService ratingStatsService;

    @GetMapping("/users/{id}")
    public List<Rating> getAllRatingByUserId(@PathVariable Long id) {
        return ratingService.getAllRatingsByUserId(id);
    }

    @PostMapping
    public void addRating(@Valid @RequestBody RatingDto ratingDto) {
        ratingService.rateMovie(ratingDto);
    }

    @GetMapping("/stats/movie/{id}")
    public RatingStats getRatingStatsByMovieId(@PathVariable Long id) {
        return ratingStatsService.findByMovieId(id);
    }

    @PostMapping("/stats/spec")
    public Rating getRatingByUserIdAndMovieId(@Valid @RequestBody GetUniqueRatingDto  getUniqueRatingDto) {
        return ratingService.getRatingByUserIdAndMovieId(getUniqueRatingDto.getUserId(), getUniqueRatingDto.getMovieId());
    }
}
