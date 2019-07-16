package com.cai.smith.videogameapi.service;

import com.cai.smith.videogameapi.exception.DataException;
import com.cai.smith.videogameapi.mapper.ResponseMapper;
import com.cai.smith.videogameapi.model.Developer;
import com.cai.smith.videogameapi.model.Game;
import com.cai.smith.videogameapi.repository.DeveloperRepository;
import com.cai.smith.videogameapi.repository.GameRepository;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private ResponseMapper responseMapper;

    private static final Logger logger =
            LoggerFactory.getLogger(GameService.class);

    public ResponseEntity create(Game game) throws DataException {
        try {
            logger.info("Checking if posted developer is authorised");
            if (checkDeveloperApproved(game)) {

                logger.info("Developer authorised - inserting into database");
                game.setGameId(createUUID());
                gameRepository.insert(game);

                return ResponseEntity.status(HttpStatus.CREATED).body(responseMapper.mapGameResponse(game));
            }
            logger.info("Developer is not part of the authorised list");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (MongoException e) {
            throw new DataException("Failed to insert game", e);
        }
    }

    public ResponseEntity getAll() throws DataException {

        try {
            List<Game> games = gameRepository.findAll();

            if (games.isEmpty()) {
                logger.info("No games found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            logger.info("Returning all games");
            return ResponseEntity.status(HttpStatus.OK).body(responseMapper.mapGameResponseList(games));

        } catch (MongoException e) {
            throw new DataException("Failed to retrieve games", e);
        }
    }

    public ResponseEntity get(String id) throws DataException {

        try {
            Game game = gameRepository.findByGameId(id);

            logger.info("Returning game");
            return ResponseEntity.status(HttpStatus.OK).body(responseMapper.mapGameResponse(game));

        } catch (MongoException e) {
            throw new DataException("Failed to retrieve game", e);
        }
    }

    public ResponseEntity update(Game newGame, String developer, String id) throws DataException {

        try {
            Game game = gameRepository.findByGameId(id);

            if (game == null) {
                logger.info("No game returned");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (developer.equalsIgnoreCase(game.getDeveloper())) {

                logger.info("Updating game with new data");
                game.setTitle(newGame.getTitle());
                game.setGenres(newGame.getGenres());
                game.setDeveloper(newGame.getDeveloper());
                game.setReleaseDate(newGame.getReleaseDate());

                gameRepository.save(game);
                logger.info("Game updated");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            logger.info("Unauthorised developer - unable to update game");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (MongoException e) {
            throw new DataException("Failed to update game", e);
        }
    }

    public ResponseEntity delete(String developer, String id) throws DataException {

        try {

            Game game = gameRepository.findByGameId(id);

            if (game == null) {
                logger.info("No game returned");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            if (developer.equalsIgnoreCase(game.getDeveloper())) {
                gameRepository.deleteByGameId(id);
                logger.info("Game deleted");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            logger.info("Unauthorised developer - unable to delete game");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (MongoException e) {
            throw new DataException("Failed to update game", e);
        }
    }

    private String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private boolean checkDeveloperApproved(Game game) {

        List<Developer> developers = developerRepository.findAll();

        for (Developer developer : developers) {

            if (game.getDeveloper().equals(developer.getName())) {
                return true;
            }
        }
        return false;
    }
}
