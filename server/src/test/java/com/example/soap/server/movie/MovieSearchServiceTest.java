package com.example.soap.server.movie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class MovieSearchServiceTest {

    private final MovieRepository repository = new MovieRepository();
    private final MovieSearchService service = new MovieSearchService(repository);
    private final Locale originalLocale = Locale.getDefault();

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(originalLocale);
    }

    @Test
    void repositoryHoldsAtMost32Movies() {
        assertThat(repository.findAll()).hasSizeBetween(1, 32);
    }

    @Test
    void nullKeywordReturnsAllMovies() {
        assertThat(service.search(null)).hasSize(repository.findAll().size());
    }

    @Test
    void blankKeywordReturnsAllMovies() {
        assertThat(service.search("   ")).hasSize(repository.findAll().size());
    }

    @Test
    void matchesDirectorIgnoringCase() {
        assertThat(titles(service.search("CARPENTER")))
                .containsExactly("Halloween", "The Thing");
    }

    @Test
    void matchesTitleIgnoringCase() {
        assertThat(titles(service.search("nightmare")))
                .containsExactly("A Nightmare on Elm Street");
    }

    @Test
    void matchesSubgenreIgnoringCase() {
        assertThat(titles(service.search("Slasher")))
                .containsExactly("The Texas Chain Saw Massacre", "Halloween",
                        "A Nightmare on Elm Street", "Scream");
    }

    @Test
    void trimsSurroundingWhitespace() {
        assertThat(titles(service.search("  carpenter  ")))
                .containsExactly("Halloween", "The Thing");
    }

    @Test
    void matchingIsLocaleIndependent() {
        Locale.setDefault(Locale.forLanguageTag("tr-TR"));
        assertThat(titles(service.search("it follows"))).containsExactly("It Follows");
    }

    @Test
    void noMatchReturnsEmptyList() {
        assertThat(service.search("xyzzy")).isEmpty();
    }

    @Test
    void resultsAreSortedByYear() {
        assertThat(service.search(null))
                .isSortedAccordingTo(Comparator.comparingInt(Movie::year));
    }

    private static List<String> titles(List<Movie> movies) {
        return movies.stream().map(Movie::title).toList();
    }
}
