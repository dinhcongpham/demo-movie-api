package com.example.demo_api_movie.EpisodeService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EpisodeCreateRequest {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Episode number is required")
    @Min(value = 1, message = "Episode number must be at least 1")
    private Integer number;

    @NotBlank(message = "Video URL is required")
    private String videoUrl;
}
