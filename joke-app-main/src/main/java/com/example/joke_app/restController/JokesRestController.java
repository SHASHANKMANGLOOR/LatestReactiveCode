package com.example.joke_app.restController;

import com.example.joke_app.dto.RandomJokeResponseDto;
import com.example.joke_app.service.JokesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class JokesRestController {

    @Autowired
    private JokesService jokesService;


    @GetMapping("/jokes")
    public Mono<List<RandomJokeResponseDto>> getJokeById(@RequestParam("count") @Valid @Min(value = 1, message = "Minimum Value is 1") final int count) {
        return jokesService.getJokesAndsaveJokes(count);
    }

}