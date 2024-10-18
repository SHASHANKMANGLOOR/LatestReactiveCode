package com.example.joke_app.restController;

import com.example.joke_app.dto.JokesDto;
import com.example.joke_app.service.JokesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/*
@RestController
public class JokesRestController {

    private static final Logger logger = LoggerFactory.getLogger(JokesRestController.class);
    @Autowired
    JokesService jokeService;

*/
/*    @GetMapping("/jokes")
    public ResponseEntity<List<JokesResponseDTO>> fetchjokesbyCount
            (@RequestParam @Valid @Min(value = 1, message = "Minimum value of 1") @NotNull(message = "Count value cant be null") int count) {
        logger.info("Entered GetMapping method");
        return ResponseEntity.ok(jokeService.getJokes(count));
    }*//*


    @GetMapping("/jokes")
    public Mono<ResponseEntity<List<JokesResponseDTO>>> fetchjokesbyCount(
            @RequestParam @Valid @Min(value = 1, message = "Minimum value of 1") @NotNull(message = "Count value cant be null") int count) {
        logger.info("Entered GetMapping method");
        return jokeService.getJokes(count)
                .map(ResponseEntity::ok)
                .doOnError(ex -> logger.error("Error fetching jokes", ex));
    }
}
*/

@RestController
public class JokesRestController {

    @Autowired
    private JokesService jokesService;


    @GetMapping("/jokes")
    public Mono<List<JokesDto>> getJokeById(@RequestParam("count") @Valid @Min(value = 1, message = "Minimum Value is 1") int count) {
        return jokesService.getJokesAndsaveJokes(count);
    }

}