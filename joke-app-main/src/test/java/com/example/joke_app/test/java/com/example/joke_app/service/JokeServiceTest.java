package com.example.joke_app.test.java.com.example.joke_app.service;


import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.dto.RandomJokeResponseDto;
import com.example.joke_app.exception.GlobalExceptionHandler;
import com.example.joke_app.exception.ServiceExcpetions;
import com.example.joke_app.service.JokeClientService;
import com.example.joke_app.service.JokesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class JokeServiceTest {

    @Mock
    private JokeClientService jokeClientService;

    @InjectMocks
    private JokesService jokeService; // Assuming this is the class containing getJokesAndsaveJokes

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetJokesAndSaveJokes_Success() {
        // Arrange
        List<JokesDto> batch1 = Arrays.asList(new JokesDto(1L, "a", "a", "Joke 1"), new JokesDto(2L, "b", "b", "Joke 2"));
        List<JokesDto> batch2 = Arrays.asList(new JokesDto(3L, "c", "c", "Joke 3"), new JokesDto(4L, "d", "d", "Joke 4"));

        when(jokeClientService.fetchRandomJokes(2)).thenReturn(Mono.just(batch1));
        when(jokeClientService.fetchRandomJokes(2)).thenReturn(Mono.just(batch2));

        // Act
        Mono<List<RandomJokeResponseDto>> result = jokeService.getJokesAndsaveJokes(4); // Use block() to get the result

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetJokesAndSaveJokes_EmptyResult() {
        // Arrange
        when(jokeClientService.fetchRandomJokes(anyInt())).thenReturn(Mono.just(Arrays.asList()));

        // Act
        List<RandomJokeResponseDto> result = jokeService.getJokesAndsaveJokes(0).block(); // Use block() to get the result

        // Assert
        assertEquals(Arrays.asList(), result);
    }

    @Test
    public void testGetJokesAndSaveJokes_SingleBatch() {
        // Arrange
        List<JokesDto> batch = Arrays.asList(new JokesDto(1L, "a", "a", "Joke 1"));
        when(jokeClientService.fetchRandomJokes(1)).thenReturn(Mono.just(batch));

        // Act
        List<RandomJokeResponseDto> result = jokeService.getJokesAndsaveJokes(1).block(); // Use block() to get the result

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetJokesAndSaveJokes_MultipleBatches() {
        // Arrange
        List<JokesDto> batch1 = Arrays.asList(new JokesDto(1L, "a", "a", "Joke 1"));
        List<JokesDto> batch2 = Arrays.asList(new JokesDto(2L, "b", "b", "Joke 2"), new JokesDto(3L, "c", "c", "Joke 3"));

        when(jokeClientService.fetchRandomJokes(1)).thenReturn(Mono.just(batch1));
        when(jokeClientService.fetchRandomJokes(2)).thenReturn(Mono.just(batch2));

        // Act
        Mono<List<RandomJokeResponseDto>> result = jokeService.getJokesAndsaveJokes(4); // Use block() to get the result

        assertNotNull(result);
    }

    @Test
    void testInvalidJoke() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ServiceExcpetions exception = new ServiceExcpetions("No Jokes");

        ResponseEntity<String> response = exceptionHandler.serviceUnavailableExcpetion(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }
}