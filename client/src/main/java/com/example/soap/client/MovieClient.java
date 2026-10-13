package com.example.soap.client;

import com.example.movies.v1.Movie;
import com.example.movies.v1.MoviePort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieClient {

    private final MoviePort port;

    public MovieClient(MoviePort port) {
        this.port = port;
    }

    /** @throws jakarta.xml.ws.WebServiceException if the SOAP call fails */
    public List<MovieDto> search(String keyword) {
        return port.searchMovies(keyword).stream()
                .map(MovieClient::toDto)
                .toList();
    }

    private static MovieDto toDto(Movie movie) {
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getYear(),
                movie.getDirector(), movie.getSubgenre());
    }
}
