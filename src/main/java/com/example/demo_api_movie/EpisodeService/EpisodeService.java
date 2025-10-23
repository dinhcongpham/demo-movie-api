package com.example.demo_api_movie.EpisodeService;

import com.example.demo_api_movie.Exception.NotFoundException;
import com.example.demo_api_movie.MeidaService.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final S3Service s3Service;

    public Episode createEpisode(EpisodeCreateRequest request) {
        Episode episode = new Episode();
        episode.setMovieId(request.getMovieId());
        episode.setTitle(request.getTitle());
        episode.setNumber(request.getNumber());
        episode.setVideoUrl(request.getVideoUrl());
        episode = episodeRepository.save(episode);

        return episode;
    }

    public List<Episode> getEpisodesByMovie(Long movieId) {
        return episodeRepository.findByMovieId(movieId);
    }

    public Episode getEpisodeById(Long episodeId) {
        return episodeRepository.findById(episodeId).orElseThrow(
                () -> new NotFoundException("Episode not found")
        );
    }

    public Episode getEpisodeByMovieIdAndNumber(Long movieId, Long number) {
        return episodeRepository.findByMovieIdAndNumber(movieId, number).orElseThrow(
                () -> new NotFoundException("Episode not found")
        );
    }

    public Episode updateEpisode(Long episodeId, Episode request) {
        Episode episode = getEpisodeById(episodeId);
        episode.setTitle(request.getTitle());
        episode.setNumber(request.getNumber());

        if (request.getVideoUrl() != episode.getVideoUrl()) {
            s3Service.deleteFile(request.getVideoUrl(), "video");
            episode.setVideoUrl(request.getVideoUrl());
        }
        episode = episodeRepository.save(episode);
        return episode;
    }

    public void deleteEpisode(Long episodeId) {
        Episode episode = getEpisodeById(episodeId);
        s3Service.deleteFile(episode.getVideoUrl(), "video");
        episodeRepository.deleteById(episodeId);
    }
}
