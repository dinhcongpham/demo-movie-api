package com.example.demo_api_movie.RatingService;

import com.example.demo_api_movie.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingStatsService {
    private final RatingStatsRepository ratingStatsRepository;

    public void updateStat(RatingDto ratingDto, boolean isUpdate, double oldRatingScore) {
        Long movieId = ratingDto.getMovieId();

        RatingStats stats = ratingStatsRepository.findById(movieId)
                .orElseGet(() -> RatingStats.builder()
                        .movieId(movieId)
                        .ratingCount(0L)
                        .totalScore(0.0)
                        .avgScore(0.0)
                        .build()
                );

        if (isUpdate) {
            double differentScore = ratingDto.getScore() - oldRatingScore;
            double newTotalScore = stats.getTotalScore() + differentScore;
            double newAvg = Math.round((newTotalScore / stats.getRatingCount()) * 100.0) / 100.0;

            stats.setTotalScore(newTotalScore);
            stats.setAvgScore(newAvg);
        } else {
            Long currentRatingCount = stats.getRatingCount();
            double newTotalScore = stats.getTotalScore() + ratingDto.getScore();
            double newAvg = Math.round((newTotalScore / (currentRatingCount + 1)) * 100.0) / 100.0;

            stats.setRatingCount(currentRatingCount + 1);
            stats.setTotalScore(newTotalScore);
            stats.setAvgScore(newAvg);
        }

        ratingStatsRepository.save(stats);
    }

    public RatingStats findByMovieId(Long movieId) {
        return ratingStatsRepository
                .findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
    }
}
