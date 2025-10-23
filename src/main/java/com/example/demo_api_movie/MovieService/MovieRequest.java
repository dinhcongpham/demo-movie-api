package com.example.demo_api_movie.MovieService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @NotBlank(message = "Background URL is required")
    private String backgroundUrl;

    @NotEmpty(message = "At least one genre is required")
    private Set<Long> genreIds; //

    @NotBlank(message = "Director is required")
    private String director;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Year release if required")
    private LocalDate releaseDate;
}