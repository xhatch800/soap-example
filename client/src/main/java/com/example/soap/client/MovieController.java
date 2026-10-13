package com.example.soap.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private final MovieClient movieClient;

    public MovieController(MovieClient movieClient) {
        this.movieClient = movieClient;
    }

    @GetMapping("/movies")
    public List<MovieDto> movies(@RequestParam(required = false) String keyword) {
        return movieClient.search(keyword);
    }
}
