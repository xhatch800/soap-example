package com.example.soap.client;

import com.example.movies.v1.Movie;
import com.example.movies.v1.MoviePort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieClientTest {

    private final MoviePort port = mock(MoviePort.class);
    private final MovieClient client = new MovieClient(port);

    @Test
    void mapsSoapMoviesToDtos() {
        when(port.searchMovies("carpenter")).thenReturn(List.of(
                soapMovie(12, "Halloween", 1978, "John Carpenter", "slasher")));

        assertThat(client.search("carpenter"))
                .containsExactly(new MovieDto(12, "Halloween", 1978, "John Carpenter", "slasher"));
    }

    @Test
    void passesNullKeywordThrough() {
        when(port.searchMovies(null)).thenReturn(List.of());

        assertThat(client.search(null)).isEmpty();
        verify(port).searchMovies(null);
    }

    private static Movie soapMovie(int id, String title, int year, String director, String subgenre) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setYear(year);
        movie.setDirector(director);
        movie.setSubgenre(subgenre);
        return movie;
    }
}
