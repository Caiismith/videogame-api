package com.cai.smith.videogameapi.repository;

import com.cai.smith.videogameapi.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {

    Game findByGameId(String id);
    void deleteByGameId(String id);
}
