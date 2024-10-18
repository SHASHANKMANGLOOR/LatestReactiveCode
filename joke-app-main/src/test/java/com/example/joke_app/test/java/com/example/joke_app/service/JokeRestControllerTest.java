package com.example.joke_app.test.java.com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.restController.JokesRestController;
import com.example.joke_app.service.JokesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(JokesRestController.class)
public class JokeRestControllerTest {

    @Autowired
    private MockMvc mock;

    @MockBean
    private JokesService service;

    @Test
    public void testJokesList() throws Exception {
        // Arrange
        List<JokesDto> mockJokes = Arrays.asList(
                new JokesDto(1L, "Setup 1", "g", "Punchline 1"),
                new JokesDto(2L, "g", "Setup 2", "Punchline 2")
        );

        // Correctly wrap the list in a Mono
        when(service.getJokesAndsaveJokes(2)).thenReturn(Mono.just(mockJokes));

        // Act & Assert
        mock.perform(get("/jokes?count=2"))
                .andExpect(status().isOk())
                ;
        mock.perform(get("/jokes?count=0"))
                .andExpect(status().is4xxClientError());
    }
}