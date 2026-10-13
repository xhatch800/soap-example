package com.example.soap.client;

import com.example.movies.v1.MoviePort;
import com.example.movies.v1.MovieService;
import jakarta.xml.ws.BindingProvider;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapClientConfig {

    @Bean
    public MoviePort moviePort(MovieServiceProperties props) {
        MoviePort port = new MovieService().getMoviePort();

        // Standard JAX-WS: override the WSDL's soap:address at runtime.
        ((BindingProvider) port).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, props.endpoint());

        // CXF-specific: transport timeouts.
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(props.connectTimeout().toMillis());
        policy.setReceiveTimeout(props.receiveTimeout().toMillis());
        ((HTTPConduit) ClientProxy.getClient(port).getConduit()).setClient(policy);

        return port;
    }
}
