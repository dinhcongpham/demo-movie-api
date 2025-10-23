package com.example.demo_api_movie.EpisodeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @PostMapping
    public Episode createEpisode(@Valid @RequestBody EpisodeCreateRequest request) {
        return episodeService.createEpisode(request);
    }

    @GetMapping("/movie/{movieId}")
    public List<Episode> getEpisodesByMovie(@PathVariable Long movieId) {
        return episodeService.getEpisodesByMovie(movieId);
    }

    @GetMapping("/{id}")
    public Episode getEpisodesById(@PathVariable Long id) {
        return episodeService.getEpisodeById(id);
    }

    @PutMapping("/{id}")
    public Episode updateEpisode(@PathVariable Long id, @RequestBody Episode episode) {
        return episodeService.updateEpisode(id, episode);
    }

    @GetMapping("/movie/{movieId}/episode/{number}")
    public Episode getEpisodeByMovieIdAndNumber(@PathVariable Long movieId, @PathVariable Long number) {
        return episodeService.getEpisodeByMovieIdAndNumber(movieId, number);
    }

    @DeleteMapping("/{id}")
    public void deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
    }
}
