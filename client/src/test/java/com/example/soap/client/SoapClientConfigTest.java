package com.example.soap.client;

import com.example.movies.v1.MoviePort;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.WebServiceException;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoapClientConfigTest {

    private final SoapClientConfig config = new SoapClientConfig();

    @Test
    void setsEndpointAddressFromProperties() {
        MovieServiceProperties props = new MovieServiceProperties(
                "http://example.test:1234/services/movies", Duration.ofSeconds(2), Duration.ofSeconds(5));

        MoviePort port = config.moviePort(props);

        assertThat(((BindingProvider) port).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY))
                .isEqualTo("http://example.test:1234/services/movies");
    }

    @Test
    void appliesTimeoutsToConduit() {
        MovieServiceProperties props = new MovieServiceProperties(
                "http://example.test:1234/services/movies", Duration.ofMillis(1500), Duration.ofSeconds(4));

        MoviePort port = config.moviePort(props);

        HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(port).getConduit();
        assertThat(conduit.getClient().getConnectionTimeout()).isEqualTo(1500);
        assertThat(conduit.getClient().getReceiveTimeout()).isEqualTo(4000);
    }

    @Test
    void unreachableEndpointFailsWithWebServiceException() {
        MovieServiceProperties props = new MovieServiceProperties(
                "http://localhost:1/services/movies", Duration.ofSeconds(1), Duration.ofSeconds(1));

        MoviePort port = config.moviePort(props);

        assertThatThrownBy(() -> port.searchMovies("x")).isInstanceOf(WebServiceException.class);
    }
}
