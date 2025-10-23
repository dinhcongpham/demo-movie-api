package com.example.demo_api_movie.MovieService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoviePreviewDto {
    private Long id;
    private String title;
    private String backgroundUrl;
}
