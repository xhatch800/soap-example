package com.example.soap.server.soap;

import com.example.movies.v1.MoviePort;
import com.example.soap.server.movie.Movie;
import com.example.soap.server.movie.MovieSearchService;
import jakarta.jws.WebService;
import org.springframework.stereotype.Component;

import java.util.List;

/** Thin SOAP adapter: implements the WSDL-generated port and maps domain → JAXB types. */
@Component
@WebService(
        serviceName = "MovieService",
        portName = "MoviePort",
        targetNamespace = "http://example.com/movies/v1",
        endpointInterface = "com.example.movies.v1.MoviePort",
        wsdlLocation = "classpath:wsdl/movies.wsdl")
public class MoviePortImpl implements MoviePort {

    private final MovieSearchService searchService;

    public MoviePortImpl(MovieSearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public List<com.example.movies.v1.Movie> searchMovies(String keyword) {
        return searchService.search(keyword).stream()
                .map(MoviePortImpl::toSoap)
                .toList();
    }

    private static com.example.movies.v1.Movie toSoap(Movie movie) {
        com.example.movies.v1.Movie soap = new com.example.movies.v1.Movie();
        soap.setId(movie.id());
        soap.setTitle(movie.title());
        soap.setYear(movie.year());
        soap.setDirector(movie.director());
        soap.setSubgenre(movie.subgenre());
        return soap;
    }
}
