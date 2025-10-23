package com.example.demo_api_movie.MovieService;

import com.example.demo_api_movie.Exception.BadRequestException;
import com.example.demo_api_movie.Exception.NotFoundException;
import com.example.demo_api_movie.MeidaService.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final S3Service s3Service;

    public MovieDto createMovie(MovieRequest request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setBackgroundUrl(request.getBackgroundUrl());
        movie.setDirector(request.getDirector());
        movie.setCountry(request.getCountry());
        movie.setReleaseDate(request.getReleaseDate());

        Set<Genre> genres = request.getGenreIds().stream()
                .map(id -> genreRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Genre not found with id " + id))
                )
                .collect(Collectors.toSet());

        movie.setGenres(genres);

        movie = movieRepository.save(movie);

        return toDto(movie);
    }


    public Page<?> getMovies(String title, List<Long> genreIds, String director,
                                 String country, Integer year, int page, int size, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("releaseDate").descending());

        Specification<Movie> spec = MovieSpecification.filterMovies(title, genreIds, director, country, year);

        Page<Movie> movies =  movieRepository.findAll(spec, pageable);

        if (Objects.equals(type, "preview")) {
            return movies.map(this::toPreviewDto);
        }
        return movies.map(this::toDto);
    }



    public MovieDto getMovieById(Long id) {
        Movie movie =  movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        return toDto(movie);
    }

    @Transactional
    public MovieDto updateMovieById(Long id, MovieRequest request) {
        Movie oldMovie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        if (!Objects.equals(oldMovie.getBackgroundUrl(), request.getBackgroundUrl())) {
            s3Service.deleteFile(getKeyS3ByFullUrl(oldMovie.getBackgroundUrl()), "image");
        }

        oldMovie.setTitle(request.getTitle());
        oldMovie.setDescription(request.getDescription());
        oldMovie.setBackgroundUrl(request.getBackgroundUrl());
        oldMovie.setDirector(request.getDirector());
        oldMovie.setCountry(request.getCountry());
        oldMovie.setReleaseDate(request.getReleaseDate());

        // Update genres
        Set<Genre> genres = request.getGenreIds().stream()
                .map(idGenre -> genreRepository.findById(idGenre)
                        .orElseThrow(() -> new NotFoundException("Genre not found with id " + idGenre))
                )
                .collect(Collectors.toSet());

        oldMovie.getGenres().clear();
        oldMovie.getGenres().addAll(genres);

        oldMovie = movieRepository.save(oldMovie);
        return toDto(oldMovie);
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        String backgroundUrl = movie.getBackgroundUrl();
        String key = getKeyS3ByFullUrl(backgroundUrl);

        s3Service.deleteFile(key, "image");
        movieRepository.deleteById(id);
    }

    private String getKeyS3ByFullUrl (String url) {
        String[] pieces = url.split("/");
        if (pieces.length == 4)
            return pieces[3];
        else {
            throw new BadRequestException("Background Image is not valid");
        }
    }

    private MovieDto toDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .backgroundUrl(movie.getBackgroundUrl())
                .director(movie.getDirector())
                .country(movie.getCountry())
                .releaseDate(movie.getReleaseDate())
                .updatedAt(movie.getUpdatedAt())
                .genreIds(
                        movie.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
                )
                .build();
    }

    private MoviePreviewDto toPreviewDto(Movie movie) {
        return MoviePreviewDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .backgroundUrl(movie.getBackgroundUrl())
                .build();
    }
}
