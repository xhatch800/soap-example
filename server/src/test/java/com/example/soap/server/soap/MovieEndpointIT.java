package com.example.soap.server.soap;

import com.example.movies.v1.Movie;
import com.example.movies.v1.MoviePort;
import com.example.movies.v1.MovieService;
import jakarta.xml.ws.BindingProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieEndpointIT {

    @Value("${local.server.port}")
    int port;

    private final HttpClient http = HttpClient.newHttpClient();

    @Test
    void servesTheHandWrittenWsdl() throws Exception {
        HttpResponse<String> response = get("/services/movies?wsdl");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("Hand-written contract-first Movie service");
    }

    @Test
    void servesTheImportedSchema() throws Exception {
        HttpResponse<String> response = get("/services/movies?xsd=movies.xsd");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("searchMoviesResponse");
    }

    @Test
    void searchesOverSoap() {
        MoviePort client = new MovieService().getMoviePort();
        ((BindingProvider) client).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://localhost:" + port + "/services/movies");

        List<Movie> result = client.searchMovies("carpenter");

        assertThat(result).extracting(Movie::getTitle).containsExactly("Halloween", "The Thing");
        assertThat(result.get(0).getYear()).isEqualTo(1978);
        assertThat(result.get(0).getDirector()).isEqualTo("John Carpenter");
    }

    @Test
    void omittedKeywordReturnsAllMoviesOverSoap() {
        MoviePort client = new MovieService().getMoviePort();
        ((BindingProvider) client).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://localhost:" + port + "/services/movies");

        assertThat(client.searchMovies(null)).hasSize(32);
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build();
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
