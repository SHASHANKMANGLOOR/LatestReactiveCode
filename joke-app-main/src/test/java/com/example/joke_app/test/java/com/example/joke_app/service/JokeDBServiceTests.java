package com.example.joke_app.test.java.com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.entity.RandomJoke;
import com.example.joke_app.exception.ServiceExcpetions;
import com.example.joke_app.repository.RandomJokeRepository;
import com.example.joke_app.service.DBService;
import com.example.joke_app.service.JokesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JokeDBServiceTests {

    @Mock
    private RandomJokeRepository jokeRepository; // Assuming this is your repository interface

    @InjectMocks
    private DBService jokeService; // Assuming this is the class containing fetchJokes

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchJokes_Success() {
        // Arrange
        JokesDto jokeDTO = new JokesDto(1L, "Setup", "g", "Punchline");
        when(jokeRepository.existsById(jokeDTO.getId())).thenReturn(false); // Simulate that the joke does not exist

        // Act
        jokeService.fetchJokes(jokeDTO);

        // Assert
        verify(jokeRepository, times(1)).save(any(RandomJoke.class)); // Verify that save was called once
    }

    @Test
    public void testFetchJokes_JokeExists() {
        // Arrange
        JokesDto jokeDTO = new JokesDto(1L, "Setup", "g", "Punchline");
        when(jokeRepository.existsById(jokeDTO.getId())).thenReturn(true); // Simulate that the joke already exists

        // Act
        jokeService.fetchJokes(jokeDTO);

        // Assert
        verify(jokeRepository, never()).save(any(RandomJoke.class)); // Verify that save was never called
    }

    @Test
    public void testFetchJokes_DatabaseError() {
        // Arrange
        JokesDto jokeDTO = new JokesDto(1L, "Setup", "g", "Punchline");
        when(jokeRepository.existsById(jokeDTO.getId())).thenReturn(false);
        doThrow(new DataAccessException("Database error") {}).when(jokeRepository).save(any(RandomJoke.class));

        // Act & Assert
        Exception exception = assertThrows(ServiceExcpetions.class, () -> {
            jokeService.fetchJokes(jokeDTO);
        });

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Database error"));
    }
}