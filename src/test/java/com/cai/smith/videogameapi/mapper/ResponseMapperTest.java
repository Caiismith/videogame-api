package com.cai.smith.videogameapi.mapper;

import com.cai.smith.videogameapi.model.Game;
import com.cai.smith.videogameapi.model.response.GameResponse;
import com.cai.smith.videogameapi.model.response.GameResponseList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResponseMapperTest {

    private ResponseMapper responseMapper = new ResponseMapper();

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final LocalDate RELEASE_DATE = LocalDate.of(2019, 01, 01);
    private static final String APPROVED = "approved";
    private static final String ACTION_GENRE = "action";
    private static final String ADVENTURE_GENRE = "adventure";

    @Test
    void mapGameResponseTest() {
        Game game = createGame();

        GameResponse gameResponse = responseMapper.mapGameResponse(game);

        assertEquals(ID, gameResponse.getId());
        assertEquals(TITLE, gameResponse.getTitle());
        assertEquals(RELEASE_DATE, gameResponse.getReleaseDate());
        assertEquals(APPROVED, gameResponse.getDeveloper());
        assertTrue(gameResponse.getGenres().contains(ACTION_GENRE));
        assertTrue(gameResponse.getGenres().contains(ADVENTURE_GENRE));
    }

    @Test
    void mapGameResponseListTest() {
        List<Game> games = createGamesList();

        GameResponseList gameResponseList = responseMapper.mapGameResponseList(games);

        assertEquals(2, gameResponseList.getItemsPerPage());
        assertEquals(0, gameResponseList.getStartIndex());
        assertEquals(2, gameResponseList.getTotalResults());
        assertEquals(2, gameResponseList.getItems().size());

    }

    private Game createGame() {
        Game game = new Game();

        game.setGameId(ID);
        game.setTitle(TITLE);
        game.setReleaseDate(RELEASE_DATE);
        game.setDeveloper(APPROVED);

        List<String> genres = new ArrayList<>();
        genres.add(ACTION_GENRE);
        genres.add(ADVENTURE_GENRE);
        game.setGenres(genres);

        return game;
    }

    private List<Game> createGamesList() {
        Game game1 = createGame();
        Game game2 = createGame();

        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);

        return games;
    }
}
