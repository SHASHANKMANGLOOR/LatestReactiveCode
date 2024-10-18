package com.example.joke_app.entity;

import com.example.joke_app.dto.JokesDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Jokes Table")
public class RandomJoke {

    @Id
    @Column(name = "Id")
    private Long id;
    @Column(name = "Setup")
    private String setup;
    @Column(name = "Punch_Line")
    private String punchline;
    @Column(name = "Type")
    private String type;

    public RandomJoke(JokesDto jokeDto) {
        this.id = jokeDto.getId();
        this.setup = jokeDto.getSetup();
        this.punchline = jokeDto.getPunchline();
        this.type = jokeDto.getType();
    }
}