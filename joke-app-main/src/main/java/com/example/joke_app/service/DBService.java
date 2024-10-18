package com.example.joke_app.service;

import com.example.joke_app.dto.JokesDBDto;
import com.example.joke_app.entity.RandomJoke;
import com.example.joke_app.exception.ServiceExcpetions;
import com.example.joke_app.repository.RandomJokeRepository;
import org.hibernate.ResourceClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.joke_app.errorConstants.ErrorConstants.Exception_OCCURED;
import static com.example.joke_app.errorConstants.ErrorConstants.FAILED_SAVE_TO_DB;

@Service
public class DBService {

    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private final RandomJokeRepository jokeRepository;


    public DBService(RandomJokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    public void fetchJokes(final JokesDBDto jokeDTO) {
        logger.info("Inside fetchJokes method to perform RestClient Call");
        try {
            RandomJoke randomJoke = new RandomJoke(jokeDTO);

            if (!jokeRepository.existsById(jokeDTO.getId())) {
                jokeRepository.save(randomJoke);
            }

        } catch (ResourceClosedException exception) {
            throw new ServiceExcpetions(FAILED_SAVE_TO_DB + exception.getMessage());
        } catch (Exception exception) {
            throw new ServiceExcpetions(Exception_OCCURED + exception.getMessage());
        }
    }

}
