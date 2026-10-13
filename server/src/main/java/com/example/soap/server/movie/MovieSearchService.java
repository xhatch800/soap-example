package com.example.soap.server.movie;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class MovieSearchService {

    private static final Comparator<Movie> BY_YEAR_THEN_TITLE =
            Comparator.comparingInt(Movie::year).thenComparing(Movie::title);

    private final MovieRepository repository;

    public MovieSearchService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<Movie> search(String keyword) {
        String needle = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return repository.findAll().stream()
                .filter(movie -> needle.isEmpty() || matches(movie, needle))
                .sorted(BY_YEAR_THEN_TITLE)
                .toList();
    }

    private static boolean matches(Movie movie, String needle) {
        return contains(movie.title(), needle)
                || contains(movie.director(), needle)
                || contains(movie.subgenre(), needle);
    }

    private static boolean contains(String field, String needle) {
        return field.toLowerCase(Locale.ROOT).contains(needle);
    }
}
