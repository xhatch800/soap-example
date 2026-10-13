package com.example.soap.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties("movies.soap")
public record MovieServiceProperties(
        @DefaultValue("http://localhost:8080/services/movies") String endpoint,
        @DefaultValue("2s") Duration connectTimeout,
        @DefaultValue("5s") Duration receiveTimeout) {
}
