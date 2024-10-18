package com.example.joke_app.repository;


import com.example.joke_app.entity.RandomJoke;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomJokeRepository extends JpaRepository<RandomJoke, Long> {
}