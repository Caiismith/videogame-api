package com.cai.smith.videogameapi.service;

import com.cai.smith.videogameapi.exception.DataException;
import com.cai.smith.videogameapi.mapper.ResponseMapper;
import com.cai.smith.videogameapi.model.Developer;
import com.cai.smith.videogameapi.model.Game;
import com.cai.smith.videogameapi.model.response.GameResponse;
import com.cai.smith.videogameapi.model.response.GameResponseList;
import com.cai.smith.videogameapi.repository.DeveloperRepository;
import com.cai.smith.videogameapi.repository.GameRepository;
import com.mongodb.MongoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {

    @Mock
    private GameRepository mockGameRepository;

    @Mock
    private DeveloperRepository mockDeveloperRepository;

    @Mock
    private ResponseMapper mockResponseMapper;

    @Mock
    private GameResponse mockGameResponse;

    @Mock
    private GameResponseList mockGameResponseList;

    @Mock
    private MongoException mockMongoException;

    @InjectMocks
    private GameService gameService;

    private static final String APPROVED_DEVELOPER = "approved developer";
    private static final String UNAPPROVED_DEVELOPER = "unapproved developer";
    private static final String ADVENTURE_GENRE = "adventure";
    private static final String ACTION_GENRE = "action";
    private static final LocalDate RELEASE_DATE = LocalDate.of(2019, 01, 01);

    @Test
    @DisplayName("Tests successful post")
    void postGame() throws DataException {

        Game game = createGame();

        when(mockDeveloperRepository.findAll()).thenReturn(createApprovedDevelopers());
        when(mockResponseMapper.mapGameResponse(game)).thenReturn(mockGameResponse);

        ResponseEntity responseEntity = gameService.create(game);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockGameResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Tests post return unauthorised with unapproved developer")
    void postGameUnapprovedDeveloper() throws DataException {

        Game game = createGameWithUnapprovedDeveloper();

        when(mockDeveloperRepository.findAll()).thenReturn(createApprovedDevelopers());

        ResponseEntity responseEntity = gameService.create(game);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test mongo exception when posting game")
    void postGameMongoException() {

        Game game = createGame();

        when(mockDeveloperRepository.findAll()).thenReturn(createApprovedDevelopers());
        when(mockGameRepository.insert(game)).thenThrow(mockMongoException);

        assertThrows(DataException.class, () -> gameService.create(game));
    }

    @Test
    @DisplayName("Tests successful getAll")
    void getAllGames() throws DataException {

        List<Game> games = createGamesList();

        when(mockGameRepository.findAll()).thenReturn(games);
        when(mockResponseMapper.mapGameResponseList(games)).thenReturn(mockGameResponseList);

        ResponseEntity responseEntity = gameService.getAll();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockGameResponseList, responseEntity.getBody());
    }

    @Test
    @DisplayName("Tests getAll with no games")
    void getAllGamesNotFound() throws DataException {

        List<Game> games = new ArrayList<>();

        when(mockGameRepository.findAll()).thenReturn(games);

        ResponseEntity responseEntity = gameService.getAll();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test mongo exception when getting all games")
    void getAllGamesMongoException() {

        when(mockGameRepository.findAll()).thenThrow(mockMongoException);

        assertThrows(DataException.class, () -> gameService.getAll());
    }

    @Test
    @DisplayName("Tests successful get")
    void getGame() throws DataException {

        Game game = createGame();

        when(mockGameRepository.findByGameId(anyString())).thenReturn(game);
        when(mockResponseMapper.mapGameResponse(game)).thenReturn(mockGameResponse);

        ResponseEntity responseEntity = gameService.get(anyString());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockGameResponse, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test mongo exception when getting game")
    void getMongoException() {

        when(mockGameRepository.findByGameId("id")).thenThrow(mockMongoException);

        assertThrows(DataException.class, () -> gameService.get("id"));
    }

    @Test
    @DisplayName("Tests successful update")
    void updateGame() throws DataException {

        Game game = createGame();

        Game updatedGame = new Game();

        updatedGame.setTitle("updated title");
        updatedGame.setReleaseDate(RELEASE_DATE);
        updatedGame.setDeveloper(APPROVED_DEVELOPER);

        List<String> genres = new ArrayList<>();
        genres.add(ACTION_GENRE);
        genres.add(ADVENTURE_GENRE);
        game.setGenres(genres);


        when(mockGameRepository.findByGameId(anyString())).thenReturn(game);

        ResponseEntity responseEntity = gameService.update(updatedGame, APPROVED_DEVELOPER, "id");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test update to game that does not exist")
    void updateGameThatDoesNotExist() throws DataException {

        Game game = createGame();

        when(mockGameRepository.findByGameId(anyString())).thenReturn(null);

        ResponseEntity responseEntity = gameService.update(game, APPROVED_DEVELOPER, "id");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Tests update return unauthorised with unapproved developer")
    void updateGameUnapprovedDeveloper() throws DataException {

        Game game = createGame();

        Game updatedGame = new Game();

        updatedGame.setTitle("updated title");
        updatedGame.setReleaseDate(RELEASE_DATE);
        updatedGame.setDeveloper(APPROVED_DEVELOPER);

        List<String> genres = new ArrayList<>();
        genres.add(ACTION_GENRE);
        genres.add(ADVENTURE_GENRE);
        game.setGenres(genres);


        when(mockGameRepository.findByGameId(anyString())).thenReturn(game);

        ResponseEntity responseEntity = gameService.update(updatedGame, UNAPPROVED_DEVELOPER, "id");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test mongo exception when updating game")
    void updateMongoException() {

        Game game = createGame();

        when(mockGameRepository.findByGameId("id")).thenThrow(mockMongoException);

        assertThrows(DataException.class, () -> gameService.update(game, APPROVED_DEVELOPER, "id"));
    }

    @Test
    @DisplayName("Tests successful delete")
    void deleteGame() throws DataException {

        Game game = createGame();

        when(mockGameRepository.findByGameId(anyString())).thenReturn(game);

        ResponseEntity responseEntity = gameService.delete(APPROVED_DEVELOPER, "id");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test delete to game that does not exist")
    void deleteGameThatDoesNotExist() throws DataException {

        when(mockGameRepository.findByGameId(anyString())).thenReturn(null);

        ResponseEntity responseEntity = gameService.delete(APPROVED_DEVELOPER, "id");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Tests delete return unauthorised with unapproved developer")
    void deleteGameUnapprovedDeveloper() throws DataException {

        Game game = createGame();

        when(mockGameRepository.findByGameId(anyString())).thenReturn(game);

        ResponseEntity responseEntity = gameService.delete(UNAPPROVED_DEVELOPER, "id");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test mongo exception when deleting game")
    void deleteMongoException() {

        Game game = createGame();

        when(mockGameRepository.findByGameId("id")).thenReturn(game);
        doThrow(mockMongoException).when(mockGameRepository).deleteByGameId("id");

        assertThrows(DataException.class, () -> gameService.delete(APPROVED_DEVELOPER, "id"));
    }

    private Game createGame() {
        Game game = new Game();

        game.setTitle("title");
        game.setReleaseDate(RELEASE_DATE);
        game.setDeveloper(APPROVED_DEVELOPER);

        List<String> genres = new ArrayList<>();
        genres.add(ACTION_GENRE);
        genres.add(ADVENTURE_GENRE);
        game.setGenres(genres);

        return game;
    }

    private Game createGameWithUnapprovedDeveloper() {
        Game game = new Game();

        game.setTitle("title");
        game.setReleaseDate(RELEASE_DATE);
        game.setDeveloper(UNAPPROVED_DEVELOPER);

        List<String> genres = new ArrayList<>();
        genres.add(ACTION_GENRE);
        genres.add(ADVENTURE_GENRE);
        game.setGenres(genres);

        return game;
    }

    private List<Game> createGamesList() {
        Game game1 = createGame();
        Game game2 = new Game();

        game2.setTitle("alternative title");
        game2.setReleaseDate(RELEASE_DATE);
        game2.setDeveloper(APPROVED_DEVELOPER);

        List<String> genres = new ArrayList<>();
        genres.add("shooter");
        game2.setGenres(genres);

        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);

        return games;
    }

    private List<Developer> createApprovedDevelopers() {
        List<Developer> developers = new ArrayList<>();
        Developer developer = new Developer();

        developer.setName(APPROVED_DEVELOPER);

        developers.add(developer);

        return developers;
    }
}
