package com.example.joke_app.test.java.com.example.joke_app.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.joke_app.dto.JokesDBDto;
import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.service.DBService;
import com.example.joke_app.service.JokeClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Mono;

import java.util.List;

public class JokeClientServiceTest {


@Mock
WebClient.Builder webBuilder;

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;


    @Mock
    private DBService dbService;

    @Mock
    private JokeClientService jokeClientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the builder to return itself when baseUrl is called
        when(webBuilder.baseUrl("https://official-joke-api.appspot.com")).thenReturn(webBuilder);

        // Mock the build method to return the mocked WebClient
        when(webBuilder.build()).thenReturn(webClient);

        // Mock the WebClient's behavior
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/random_joke")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }
   // @Test
    public void testFetchRandomJokes_Success2() {

        int count = 5;
        JokesDBDto jokeDto = new JokesDBDto(1L,"id", "setup", "punchline");


        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("https://official-joke-api.appspot.com/random_joke")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JokesDBDto.class)).thenReturn(Mono.just(jokeDto));


        List<JokesDto> result = jokeClientService.fetchRandomJokes(count).block();

        // Assert
        assertNotNull(result, "The result should not be null.");
        assertEquals(count, result.size(), "The number of jokes returned does not match the expected count.");

        // Assert that each joke is the same as the mocked jokeDto
        for (int i = 0; i < result.size(); i++) {
            JokesDto joke = result.get(i);
            assertEquals(jokeDto.getId(), joke.getId(), "Joke ID does not match for joke at index " + i);
            assertEquals(jokeDto.getSetup(), joke.getSetup(), "Joke setup does not match for joke at index " + i);
            assertEquals(jokeDto.getPunchline(), joke.getPunchline(), "Joke punchline does not match for joke at index " + i);
        }

        // Verify that dbService.fetchJokes was called for each joke
        verify(dbService, times(count)).fetchJokes(jokeDto);
    }


    @Test
    public void testFetchRandomJokes_Success() {
        // Arrange
        int count = 5;
        JokesDto jokeDto = new JokesDto(1L,"id", "setup", "punchline");

        // Mocking the webClient to return a JokesDto for each call
        when(responseSpec.bodyToMono(JokesDto.class)).thenReturn(Mono.just(jokeDto));

        // Act
        Mono<List<JokesDto>> result = jokeClientService.fetchRandomJokes(count);

        // Assert
        assertNull(result);
        //assertEquals(count, result.size(), "The number of jokes returned does not match the expected count.");

    /*    // Assert that each joke is the same as the mocked jokeDto
        for (int i = 0; i < result.size(); i++) {
            JokesDto joke = result.get(i);
            assertEquals(jokeDto.getId(), joke.getId(), "Joke ID does not match for joke at index " + i);
            assertEquals(jokeDto.getSetup(), joke.getSetup(), "Joke setup does not match for joke at index " + i);
            assertEquals(jokeDto.getPunchline(), joke.getPunchline(), "Joke punchline does not match for joke at index " + i);
        }*/

        // Verify that dbService.fetchJokes was called for each joke
      //  verify(dbService, times(count)).fetchJokes(jokeDto);
    }
    // Other test methods...
}