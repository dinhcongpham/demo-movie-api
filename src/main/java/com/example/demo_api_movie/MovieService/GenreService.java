package com.example.demo_api_movie.MovieService;

import com.example.demo_api_movie.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;


    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found with id " + id));
    }

    public Genre createGenre(Genre genre) {
        if (genreRepository.existsByName(genre.getName())) {
            throw new IllegalArgumentException("Genre with name '" + genre.getName() + "' already exists");
        }
        return genreRepository.save(genre);
    }

    public Genre updateGenre(Long id, Genre updatedGenre) {
        Genre existing = getGenreById(id);
        existing.setName(updatedGenre.getName());
        return genreRepository.save(existing);
    }

    public void deleteGenre(Long id) {
        Genre existing = getGenreById(id);
        genreRepository.delete(existing);
    }
}