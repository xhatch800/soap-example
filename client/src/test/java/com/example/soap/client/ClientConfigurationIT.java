package com.example.soap.client;

import com.example.movies.v1.MoviePort;
import jakarta.xml.ws.BindingProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "movies.soap.endpoint=http://override.test:9999/services/movies")
class ClientConfigurationIT {

    @Autowired
    MoviePort moviePort;

    @Test
    void endpointPropertyOverridesWsdlAddress() {
        assertThat(((BindingProvider) moviePort).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY))
                .isEqualTo("http://override.test:9999/services/movies");
    }
}
