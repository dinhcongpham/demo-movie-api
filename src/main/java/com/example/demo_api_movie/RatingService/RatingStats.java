package com.example.demo_api_movie.RatingService;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "movie_rating_stats")
public class RatingStats {
    @Id
    private Long movieId;

    private Long ratingCount;
    private Double totalScore;
    private Double avgScore;
}
