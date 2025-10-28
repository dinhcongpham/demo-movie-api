package com.example.demo_api_movie.RatingService;

import com.example.demo_api_movie.Exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingStatsService ratingStatsService;

    @InjectMocks
    private RatingService ratingService;

    private RatingDto ratingDto;
    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ratingDto = RatingDto.builder()
                .movieId(1L)
                .userId(2L)
                .score(4.5)
                .build();

        rating = Rating.builder()
                .movieId(1L)
                .userId(2L)
                .score(3.0)
                .build();
    }

    // --- rateMovie() ---

    @Test
    void rateMovie_shouldCreateNewRating_ifNotExists() {
        when(ratingRepository.existsByUserIdAndMovieId(2L, 1L)).thenReturn(false);

        ratingService.rateMovie(ratingDto);

        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(ratingStatsService, times(1)).updateStat(ratingDto, false, 0.0);
    }

    @Test
    void rateMovie_shouldUpdateExistingRating_ifExists() {
        when(ratingRepository.existsByUserIdAndMovieId(2L, 1L)).thenReturn(true);
        when(ratingRepository.findByUserIdAndMovieId(2L, 1L)).thenReturn(Optional.of(rating));

        ratingService.rateMovie(ratingDto);

        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(ratingStatsService, times(1)).updateStat(ratingDto, true, 3.0);
    }

    // --- createNewRating() ---

    @Test
    void createNewRating_shouldSaveRating() {
        ratingService.createNewRating(ratingDto);
        verify(ratingRepository).save(argThat(r ->
                r.getMovieId().equals(1L) &&
                        r.getUserId().equals(2L) &&
                        r.getScore() == 4.5
        ));
    }

    // --- updateRating() ---

    @Test
    void updateRating_shouldUpdateScore() {
        when(ratingRepository.findByUserIdAndMovieId(2L, 1L)).thenReturn(Optional.of(rating));

        ratingService.updateRating(ratingDto);

        assertThat(rating.getScore()).isEqualTo(4.5);
        verify(ratingRepository).save(rating);
    }

    @Test
    void updateRating_shouldThrowNotFound_ifRatingNotExists() {
        when(ratingRepository.findByUserIdAndMovieId(2L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.updateRating(ratingDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Rating not found");
    }

    // --- getRatingByUserIdAndMovieId() ---

    @Test
    void getRatingByUserIdAndMovieId_shouldReturnRating() {
        when(ratingRepository.findByUserIdAndMovieId(2L, 1L)).thenReturn(Optional.of(rating));

        Rating result = ratingService.getRatingByUserIdAndMovieId(2L, 1L);

        assertThat(result).isEqualTo(rating);
    }

    @Test
    void getRatingByUserIdAndMovieId_shouldThrow_ifNotFound() {
        when(ratingRepository.findByUserIdAndMovieId(2L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.getRatingByUserIdAndMovieId(2L, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Rating not found");
    }

    // --- getAllRatingsByUserId() ---

    @Test
    void getAllRatingsByUserId_shouldReturnList() {
        when(ratingRepository.findAllByUserId(2L)).thenReturn(List.of(rating));

        List<Rating> result = ratingService.getAllRatingsByUserId(2L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(rating);
    }
}
