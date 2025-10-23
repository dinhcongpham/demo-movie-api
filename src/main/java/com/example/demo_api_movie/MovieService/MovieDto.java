package com.example.demo_api_movie.MovieService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private String backgroundUrl;
    private String director;
    private String country;
    private Set<Long> genreIds;
    private LocalDate releaseDate;
    private LocalDate updatedAt;
}
