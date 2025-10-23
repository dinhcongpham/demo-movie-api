package com.example.demo_api_movie.RatingService;

import com.example.demo_api_movie.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingStatsService ratingStatsService;

    @Transactional
    public void rateMovie(RatingDto ratingDto) {
        double oldRatingScore = 0.0;

        boolean isUpdate = ratingRepository.existsByUserIdAndMovieId(ratingDto.getUserId(), ratingDto.getMovieId());
        if (isUpdate) {
            Rating oldRating = getRatingByUserIdAndMovieId(ratingDto.getUserId(), ratingDto.getMovieId());
            oldRatingScore = oldRating.getScore();
            updateRating(ratingDto);
        } else {
            createNewRating(ratingDto);
        }

        ratingStatsService.updateStat(ratingDto, isUpdate, oldRatingScore);
    }

    public void createNewRating(RatingDto ratingDto) {
        Rating rating = Rating.builder()
                .movieId(ratingDto.getMovieId())
                .userId(ratingDto.getUserId())
                .score(ratingDto.getScore())
                .build();

        ratingRepository.save(rating);
    }

    public void updateRating(RatingDto ratingDto) {
        Rating rating = getRatingByUserIdAndMovieId(ratingDto.getUserId(), ratingDto.getMovieId());

        rating.setScore(ratingDto.getScore());
        ratingRepository.save(rating);
    }



    public Rating getRatingByUserIdAndMovieId(Long userId, Long movieId) {
       return ratingRepository
                .findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));
    }

    public List<Rating> getAllRatingsByUserId(Long userId) {
        return ratingRepository.findAllByUserId(userId);
    }
}
