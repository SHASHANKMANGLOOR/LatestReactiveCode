package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDBDto;
import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.exception.RestClientException;
import com.example.joke_app.exception.ServiceExcpetions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    /*public Mono<List<JokesDto>> fetchRandomJokes(int count) {
        logger.info("Inside fetchRandomJokes");
        try {
            return Flux.range(0, count)
                    .flatMap(i -> webClient.get()
                            .uri("/random_joke")
                            .retrieve()
                            .onStatus(HttpStatus::isError,response ->
                                Mono.error(new restClientExcepion(Exception_OCCURED)))
                            .bodyToMono(JokesDto.class))
                    .collectList()
                    .doOnNext(jokes -> jokes.forEach(jokeDTO -> dbService.fetchJokes(jokeDTO)));
        } catch (RestClientException exception) {
            throw new ServiceExcpetions( exception.getMessage());
        } catch (Exception exception) {
            throw new ServiceExcpetions(Exception_OCCURED + exception.getMessage());
        }
    }*/

    public Mono<List<JokesDto>> fetchRandomJokes(int count) {
        logger.info("Inside fetchRandomJokes");
        try {
            return Flux.range(0, count)
                    .flatMap(i -> webClient.get()
                            .uri("/random_joke")
                            .retrieve()
                            .bodyToMono(JokesDto.class))
                    .collectList()
                    .doOnNext(jokes -> jokes.forEach(jokeDTO -> {
                        JokesDBDto jokesDBDto = new JokesDBDto();
                        jokesDBDto.setId(jokeDTO.getId());
                        jokesDBDto.setType(jokeDTO.getType());
                        jokesDBDto.setPunchline(jokeDTO.getPunchline());
                        jokesDBDto.setSetup(jokeDTO.getSetup());
                        dbService.fetchJokes(jokesDBDto);
                    }));
        } catch (RestClientException exception) {// catches all restclient
            throw new RestClientException(exception.getMessage());
        } catch (Exception exception) {
            throw new ServiceExcpetions("Exception occurred: " + exception.getMessage());
        }
    }

}