package com.example.joke_app.test.java.com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.dto.RandomJokeResponseDto;
import com.example.joke_app.exception.RestClientException;
import com.example.joke_app.exception.ServiceExcpetions;
import com.example.joke_app.restController.JokesRestController;
import com.example.joke_app.service.JokesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        public void testGetJokeById_Success() throws Exception {
            // Arrange
            int count = 5;
            RandomJokeResponseDto joke = new RandomJokeResponseDto(1, "Setup", "Punchline");
            when(service.getJokesAndsaveJokes(count)).thenReturn(Mono.just(Collections.singletonList(joke)));

            // Act & Assert
            mock.perform(get("/jokes")
                            .param("count", String.valueOf(count))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        public void testGetJokeById_InvalidCount() throws Exception {
            // Act & Assert
            mock.perform(get("/jokes")
                            .param("count", "0") // Invalid count
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertFalse(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

    @Test
    public void testGetJokeById_ServiceException2() throws Exception {
        // Arrange
        int count = 5;
        when(service.getJokesAndsaveJokes(count)).thenReturn(Mono.error(new RestClientException("Service error")));

        // Act & Assert
        mock.perform(get("/jokes")
                        .param("count", String.valueOf(5555555))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

        @Test
        public void testGetJokeById_ServiceException() throws Exception {
            // Arrange
            int count = 5;
            when(service.getJokesAndsaveJokes(count)).thenReturn(Mono.error(new ServiceExcpetions("Service error")));

            // Act & Assert
            mock.perform(get("/jokes")
                            .param("count", String.valueOf(-5))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }
    }
