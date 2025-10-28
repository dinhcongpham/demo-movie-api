package com.example.demo_api_movie.MovieService;

import com.example.demo_api_movie.Exception.BadRequestException;
import com.example.demo_api_movie.Exception.NotFoundException;
import com.example.demo_api_movie.MeidaService.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------------------
    @Test
    void createMovie_shouldSaveAndReturnDto() {
        // Arrange
        Genre genre = new Genre();
        genre.setId(1L);

        MovieRequest request = new MovieRequest();
        request.setTitle("Inception");
        request.setDescription("Dream movie");
        request.setBackgroundUrl("https://cdn.com/img/1");
        request.setDirector("Nolan");
        request.setCountry("US");
        request.setReleaseDate(LocalDate.of(2010, 7, 16));
        request.setGenreIds(Set.of(1L));

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        Movie saved = new Movie();
        saved.setId(10L);
        saved.setTitle("Inception");
        saved.setDescription("Dream movie");
        saved.setBackgroundUrl("https://cdn.com/img/1");
        saved.setDirector("Nolan");
        saved.setCountry("US");
        saved.setReleaseDate(LocalDate.of(2010, 7, 16));
        saved.setGenres(Set.of(genre));

        when(movieRepository.save(any(Movie.class))).thenReturn(saved);

        // Act
        MovieDto result = movieService.createMovie(request);

        // Assert
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTitle()).isEqualTo("Inception");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    // ------------------------------------------------------------------------
    @Test
    void createMovie_shouldThrowNotFound_ifGenreMissing() {
        MovieRequest request = new MovieRequest();
        request.setGenreIds(Set.of(2L));
        when(genreRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.createMovie(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Genre not found");
    }

    // ------------------------------------------------------------------------
    @Test
    void getMovieById_shouldReturnDto_whenFound() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Avatar");
        movie.setGenres(new HashSet<>());

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        MovieDto dto = movieService.getMovieById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Avatar");
    }

    // ------------------------------------------------------------------------
    @Test
    void getMovieById_shouldThrowNotFound_whenMissing() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getMovieById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Movie not found");
    }

    // ------------------------------------------------------------------------
    @Test
    void deleteMovie_shouldDeleteAndRemoveS3File() {
        Movie movie = new Movie();
        movie.setId(5L);
        movie.setBackgroundUrl("https://bucket.com/img/123");

        when(movieRepository.findById(5L)).thenReturn(Optional.of(movie));

        movieService.deleteMovie(5L);

        verify(s3Service, times(1)).deleteFile("123", "image");
        verify(movieRepository, times(1)).deleteById(5L);
    }

    // ------------------------------------------------------------------------
    @Test
    void deleteMovie_shouldThrow_whenNotFound() {
        when(movieRepository.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.deleteMovie(77L))
                .isInstanceOf(NotFoundException.class);
    }

    // ------------------------------------------------------------------------
    @Test
    void getKeyS3ByFullUrl_shouldReturnLastPart() throws Exception {
        String url = "https://bucket.com/img/123";

        Method method = MovieService.class.getDeclaredMethod("getKeyS3ByFullUrl", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(movieService, url);

        assertThat(result).isEqualTo("123");
    }

    @Test
    void getKeyS3ByFullUrl_shouldThrowException_ifNull() throws Exception {
        Method method = MovieService.class.getDeclaredMethod("getKeyS3ByFullUrl", String.class);
        method.setAccessible(true);

        Throwable thrown = catchThrowable(() -> {
            try {
                method.invoke(movieService, (Object) null);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertThat(thrown)
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void getKeyS3ByFullUrl_shouldReturnEmpty_ifEmptyString() throws Exception {
        Method method = MovieService.class.getDeclaredMethod("getKeyS3ByFullUrl", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(movieService, "");

        assertThat(result).isEmpty();
    }

    // ------------------------------------------------------------------------
    @Test
    void updateMovie_shouldReplaceBackground_whenChanged() {
        Movie old = new Movie();
        old.setId(1L);
        old.setBackgroundUrl("https://bucket.com/img/old");

        Genre genre = new Genre();
        genre.setId(3L);

        MovieRequest req = new MovieRequest();
        req.setBackgroundUrl("https://bucket.com/img/new");
        req.setGenreIds(Set.of(3L));

        when(movieRepository.findById(1L)).thenReturn(Optional.of(old));
        when(genreRepository.findById(3L)).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenReturn(old);

        movieService.updateMovieById(1L, req);

        verify(s3Service, times(1)).deleteFile("old", "image");
        verify(movieRepository, times(1)).save(any(Movie.class));
    }
}
