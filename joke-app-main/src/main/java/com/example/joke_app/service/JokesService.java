package com.example.joke_app.service;

import com.example.joke_app.dto.RandomJokeResponseDto;
import com.example.joke_app.exception.ServiceExcpetions;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.joke_app.errorConstants.ErrorConstants.Exception_OCCURED;

//import static com.example.joke_app.errorConstants.ErrorConstants.FAILED_FETCH;

@Service
public class JokesService {

    private static final Logger logger = LoggerFactory.getLogger(JokesService.class);

    @Autowired
    private JokeClientService jokeClientService;

    public Mono<List<RandomJokeResponseDto>> getJokesAndsaveJokes(@NotNull final int count) {
        logger.info("Fetching random jokes");
        try {
            return Flux.range(0, (count + 9) / 10) // Calculate the number of batches needed
                    .flatMap(i -> {
                        int batchSize = Math.min(10, count - i * 10);
                        return jokeClientService.fetchRandomJokes(batchSize) // Assuming this returns Mono<List<JokesDto>>
                                .map(jokes -> jokes.stream()
                                        .map(joke -> new RandomJokeResponseDto(joke.getId(), joke.getSetup(), joke.getPunchline()))
                                        .collect(Collectors.toList())); // Map JokesDto to RandomJokeResponseDto
                    })
                    .collectList() // Collect all lists of jokes into a single list
                    .flatMap(jokesList -> {
                        // Flatten the list of lists into a single list
                        List<RandomJokeResponseDto> allJokes = jokesList.stream()
                                .flatMap(List::stream)
                                .collect(Collectors.toList());
                        return Mono.just(allJokes); // Return the flattened list as a Mono
                    });
        } catch (RestClientException exception) {
            throw new RestClientException(exception.getMessage());
        } catch (Exception exception) {
            throw new ServiceExcpetions(Exception_OCCURED + exception.getMessage());
        }
    }

}

