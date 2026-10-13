package com.example.soap.client;

import jakarta.xml.ws.WebServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    MovieClient movieClient;

    @Test
    void returnsMoviesAsJson() throws Exception {
        when(movieClient.search("carpenter")).thenReturn(List.of(
                new MovieDto(12, "Halloween", 1978, "John Carpenter", "slasher")));

        mvc.perform(get("/movies").param("keyword", "carpenter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(12))
                .andExpect(jsonPath("$[0].title").value("Halloween"))
                .andExpect(jsonPath("$[0].year").value(1978))
                .andExpect(jsonPath("$[0].director").value("John Carpenter"))
                .andExpect(jsonPath("$[0].subgenre").value("slasher"));
    }

    @Test
    void keywordIsOptional() throws Exception {
        when(movieClient.search(null)).thenReturn(List.of());

        mvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void soapFailureBecomesBadGateway() throws Exception {
        when(movieClient.search(any())).thenThrow(new WebServiceException("Could not send Message."));

        mvc.perform(get("/movies").param("keyword", "x"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").value("SOAP service call failed"))
                .andExpect(jsonPath("$.detail").value("Could not send Message."));
    }
}
