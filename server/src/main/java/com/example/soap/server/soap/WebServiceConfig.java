package com.example.soap.server.soap;

import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;

@Configuration
public class WebServiceConfig {

    private static final String NAMESPACE = "http://example.com/movies/v1";

    @Bean
    public Endpoint movieEndpoint(Bus bus, MoviePortImpl moviePort) {
        EndpointImpl endpoint = new EndpointImpl(bus, moviePort);
        endpoint.setWsdlLocation("classpath:wsdl/movies.wsdl");
        endpoint.setServiceName(new QName(NAMESPACE, "MovieService"));
        endpoint.setEndpointName(new QName(NAMESPACE, "MoviePort"));
        endpoint.publish("/movies");
        return endpoint;
    }
}
