package com.example.joke_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RandomJokeResponseDto {

    private long id;
    private String question;
    private String answer;
}
