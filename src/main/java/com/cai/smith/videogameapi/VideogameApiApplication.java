package com.cai.smith.videogameapi;

import com.cai.smith.videogameapi.exception.FileDownloaderException;
import com.cai.smith.videogameapi.model.Developer;
import com.cai.smith.videogameapi.model.Developers;
import com.cai.smith.videogameapi.repository.DeveloperRepository;
import com.cai.smith.videogameapi.repository.GameRepository;
import com.cai.smith.videogameapi.utility.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VideogameApiApplication implements CommandLineRunner {

    public static final String APPLICATION_NAME_SPACE = "company-accounts.api.ch.gov.uk";

    private static final Logger logger =
            LoggerFactory.getLogger(VideogameApiApplication.APPLICATION_NAME_SPACE);

    @Autowired
    private FileDownloader fileDownloader;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    public static void main(String[] args) {
        SpringApplication.run(VideogameApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("Cleaning database");
        gameRepository.deleteAll();
        developerRepository.deleteAll();

        try {
            logger.info("Attempting to retrieve list of approved developers");
            Developers developers = fileDownloader.getApprovedDevelopers();

            for (Developer developer : developers.getDevelopers()) {
                developerRepository.save(developer);
            }
            logger.info("Approved developers stored in mongo");

        } catch (FileDownloaderException e) {
            logger.error("Failed to download approved developers list");

            developerRepository.save(createDefaultApprovedDeveloper());
            logger.info("Default developer provided");
        }
    }

    private Developer createDefaultApprovedDeveloper() {
        logger.info("Approved developers list unavailable - Providing default entry");

        Developer defaultApprovedDeveloper = new Developer();
        defaultApprovedDeveloper.setName("Nintendo");
        defaultApprovedDeveloper.setHeadquarters("Japan");

        return defaultApprovedDeveloper;
    }
}
