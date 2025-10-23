package com.example.demo_api_movie.RatingService;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDto {
    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;

    private Long userId;

    @NotNull(message = "Score cannot be null")
    @DecimalMin(value = "0.0", message = "Score must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Score must be at most 10.0")
    private Double score;
}
