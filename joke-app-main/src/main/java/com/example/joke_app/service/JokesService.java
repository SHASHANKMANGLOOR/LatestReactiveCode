package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

//import static com.example.joke_app.errorConstants.ErrorConstants.FAILED_FETCH;

@Service
public class JokesService {

    private static final Logger logger = LoggerFactory.getLogger(JokesService.class);

    @Autowired
    private JokeClientService jokeClientService;

    public Mono<List<JokesDto>> getJokesAndsaveJokes(@NotNull final int count) {
        final int batches = (int) Math.ceil((double) count / 10);

        return Flux.range(0, batches)
                .flatMap(i -> {
                    int batchSize = Math.min(10, count - i * 10);
                    return jokeClientService.fetchRandomJokes(batchSize); // Assuming this returns Mono<List<JokesDto>>
                })
                .collectList() // Collect all lists of jokes into a single list
                .flatMap(jokesList -> {
                    // Flatten the list of lists into a single list
                    List<JokesDto> allJokes = jokesList.stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
                    return Mono.just(allJokes); // Return the flattened list as a Mono
                });
    }
}

