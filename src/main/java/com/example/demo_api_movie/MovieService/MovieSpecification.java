package com.example.demo_api_movie.MovieService;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {

    public static Specification<Movie> filterMovies(
            String title,
            List<Long> genreIds,
            String director,
            String country,
            Integer year) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(
                        cb.like(
                                cb.function("unaccent", String.class, cb.lower(root.get("title"))),
                                "%" + removeAccents(title.toLowerCase()) + "%"
                        )
                );
            }


            if (genreIds != null && !genreIds.isEmpty()) {
                var genreJoin = root.join("genres");
                predicates.add(genreJoin.get("id").in(genreIds));
                query.distinct(true);
            }

            if (director != null && !director.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("director")), "%" + director.toLowerCase() + "%"));
            }

            if (country != null && !country.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("country")), "%" + country.toLowerCase() + "%"));
            }

            if (year != null) {
                predicates.add(
                        cb.equal(
                                cb.function("year", Integer.class, root.get("releaseDate")),
                                year
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String removeAccents(String input) {
        return java.text.Normalizer
                .normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
