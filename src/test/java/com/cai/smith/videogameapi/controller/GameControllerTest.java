package com.cai.smith.videogameapi.controller;

import com.cai.smith.videogameapi.exception.DataException;
import com.cai.smith.videogameapi.model.Game;
import com.cai.smith.videogameapi.model.response.GameResponse;
import com.cai.smith.videogameapi.model.response.GameResponseList;
import com.cai.smith.videogameapi.service.GameService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameControllerTest {

    @Mock
    private GameService mockGameService;

    @Mock
    private GameResponse mockGameResponse;

    @Mock
    private GameResponseList mockGameResponseList;

    @Mock
    private DataException mockDataException;

    @InjectMocks
    private GameController gameController;

    private static final String APPROVED_DEVELOPER = "approved developer";
    private static final String DEVELOPER = "developer";
    private static final LocalDate RELEASE_DATE = LocalDate.of(2019, 01, 01);

    @Test
    @DisplayName("Tests successful create")
    void create() throws DataException {

        Game game = createGame();

        when(mockGameService.create(game))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                        .body(mockGameResponse));

        ResponseEntity returnedResponse = gameController.create(game);

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.CREATED, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test create throws exception")
    void createException() throws DataException {

        Game game = createGame();

        when(mockGameService.create(game)).thenThrow(mockDataException);

        ResponseEntity returnedResponse = gameController.create(game);

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.BAD_REQUEST, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Tests successful getAll")
    void getAll() throws DataException {

        when(mockGameService.getAll())
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(mockGameResponseList));

        ResponseEntity returnedResponse = gameController.getAll();

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.OK, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test getAll throws exception")
    void getAllException() throws DataException {

        when(mockGameService.getAll()).thenThrow(mockDataException);

        ResponseEntity returnedResponse = gameController.getAll();

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.BAD_REQUEST, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Tests successful get")
    void get() throws DataException {

        when(mockGameService.get("id"))
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                        .body(mockGameResponse));

        ResponseEntity returnedResponse = gameController.get("id");

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.OK, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Test get throws exception")
    void getException() throws DataException {

        when(mockGameService.get("id")).thenThrow(mockDataException);

        ResponseEntity returnedResponse = gameController.get("id");

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.BAD_REQUEST, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Tests successful update")
    void put() throws DataException {

        Game game = createGame();

        when(mockGameService.update(game, DEVELOPER, "id"))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        ResponseEntity returnedResponse = gameController.put(DEVELOPER, "id", game);

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.NO_CONTENT, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Tests update throws exception")
    void putException() throws DataException {

        Game game = createGame();

        when(mockGameService.update(game, DEVELOPER, "id")).thenThrow(mockDataException);

        ResponseEntity returnedResponse = gameController.put(DEVELOPER, "id", game);

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.BAD_REQUEST, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Tests successful delete")
    void delete() throws DataException {

        when(mockGameService.delete(DEVELOPER, "id"))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        ResponseEntity returnedResponse = gameController.delete(DEVELOPER, "id");

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.NO_CONTENT, returnedResponse.getStatusCode());
    }

    @Test
    @DisplayName("Tests delete throws exception")
    void deleteException() throws DataException {

        when(mockGameService.delete(DEVELOPER, "id")).thenThrow(mockDataException);

        ResponseEntity returnedResponse = gameController.delete(DEVELOPER, "id");

        assertNotNull(returnedResponse);
        assertEquals(HttpStatus.BAD_REQUEST, returnedResponse.getStatusCode());
    }

    private Game createGame() {
        Game game = new Game();

        game.setTitle("title");
        game.setReleaseDate(RELEASE_DATE);
        game.setDeveloper(APPROVED_DEVELOPER);

        List<String> genres = new ArrayList<>();
        genres.add("action");
        genres.add("adventure");
        game.setGenres(genres);

        return game;
    }

}
