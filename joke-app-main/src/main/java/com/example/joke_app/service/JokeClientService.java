package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.exception.ServiceExcpetions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.joke_app.errorConstants.ErrorConstants.Exception_OCCURED;
import static com.example.joke_app.errorConstants.ErrorConstants.TOO_MANY_REQUESTS;

@Service
public class JokeClientService {

    private static final String URL = "https://official-joke-api.appspot.com";
    private static final Logger logger = LoggerFactory.getLogger(JokeClientService.class);
    private final WebClient webClient;
    @Autowired
    private DBService dbService;

    @Autowired
    public JokeClientService(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(URL).build();

    }

    public Mono<List<JokesDto>> fetchRandomJokes(int count) {
        logger.info("hi");
        try {
            return Flux.range(0, count)
                    .flatMap(i -> webClient.get()
                            .uri("/random_joke")
                            .retrieve()
                            .bodyToMono(JokesDto.class))
                    .collectList()
                    .doOnNext(jokes -> jokes.forEach(jokeDTO -> dbService.fetchJokes(jokeDTO)));
        } catch (RestClientException exception) {
            throw new ServiceExcpetions(TOO_MANY_REQUESTS + exception.getMessage());
        } catch (Exception exception) {
            throw new ServiceExcpetions(Exception_OCCURED + exception.getMessage());
        }
    }

}