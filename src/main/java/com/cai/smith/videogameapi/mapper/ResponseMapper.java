package com.cai.smith.videogameapi.mapper;

import com.cai.smith.videogameapi.model.Game;
import com.cai.smith.videogameapi.model.response.GameResponse;
import com.cai.smith.videogameapi.model.response.GameResponseList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResponseMapper {

    public GameResponse mapGameResponse(Game game) {

        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getGameId());
        gameResponse.setTitle(game.getTitle());
        gameResponse.setReleaseDate(game.getReleaseDate());
        gameResponse.setGenres(game.getGenres());
        gameResponse.setDeveloper(game.getDeveloper());

        return gameResponse;
    }

    public GameResponseList mapGameResponseList(List<Game> games) {

        GameResponseList gameResponseList = new GameResponseList();
        gameResponseList.setItemsPerPage(games.size());
        gameResponseList.setStartIndex(0);
        gameResponseList.setTotalResults(games.size());

        List<GameResponse> gameResponses = new ArrayList<>();

        for (Game game : games) {
            gameResponses.add(mapGameResponse(game));
        }

        gameResponseList.setItems(gameResponses);
        return gameResponseList;
    }

}
