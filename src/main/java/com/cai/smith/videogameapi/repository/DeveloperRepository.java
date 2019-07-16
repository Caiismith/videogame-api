package com.cai.smith.videogameapi.repository;

import com.cai.smith.videogameapi.model.Developer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeveloperRepository extends MongoRepository<Developer, String> {
}
