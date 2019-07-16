package com.cai.smith.videogameapi.controller;

import com.cai.smith.videogameapi.exception.DataException;
import com.cai.smith.videogameapi.model.Game;
import com.cai.smith.videogameapi.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    private static final Logger logger =
            LoggerFactory.getLogger(GameController.class);

    @PostMapping
    public ResponseEntity create(@RequestBody Game game) {

        try {
            logger.info("Attempting to post game");
            return gameService.create(game);
        } catch(DataException de) {
            logger.error("An error occured when attempting to post the game");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity getAll() {

        try {
            logger.info("Attempting to retreive all games");
            return gameService.getAll();
        } catch (DataException de) {
            logger.error("An error occured when attempting to get all games");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable String id) {

        try {
            return gameService.get(id);
        } catch (DataException de) {
            logger.error("An error occured when attempting to retrieve game");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/developer/{developer}/{id}")
    public ResponseEntity put(@PathVariable String developer,
                              @PathVariable String id,
                              @RequestBody Game game) {

        try {
            return gameService.update(game, developer, id);
        } catch (DataException de) {
            logger.error("An error occured when attempting to update game");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/developer/{developer}/{id}")
    public ResponseEntity delete(@PathVariable String developer,
                                 @PathVariable String id) {

        try {
            return gameService.delete(developer, id);
        } catch (DataException de) {
            logger.error("An error occured when attempting to delete game");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
