# videogame-api
# Senior Developer Interview Assignment

Provides CRUD functionality for video game data which is stored in a Mongo database. Also retrieves data on the video game developers from an Amazon S3 bucket to authorise functions within the API - e.g only a developer of a game can update thier own game.

## Requirements
- Java 8
- Maven
- MongoDB
- Git

## How to run
- With mongodb installed, open a window in your terminal and run `mongod --port 12345`
- Within your IDE - Click Run on the `VideogameApiApplication` class
- Alternatively from the command line - go to the `videogame-api` repoistory and run `mvn spring-boot:run`
- This will be running on port `8080`
- To run tests within your IDE - Find the test folder and click run on the child packages
- Alternatively from the command line - go to the `videogame-api` repository and run `mvn test`
- The `developers` collection in mongo will be populated from the s3 bucket
- Upon start up the `games` collection will be empty - the following can be used to post to the api to begin to populate the database

`POST` request to `localhost:8080/games/`

```
{
  "title" : "Halo: Combat Evolved",
  "release_date" : "2001-11-15",
  "genres" : [
      "First Person Shooter",
      "Action",
      "Sci-fi",
      "Multiplayer"
  ],
  "developer" : "Bungie"
}
```
## Example Requests
 - `POST` - `localhost:8080/games/` - including a valid body (see example above)
 - `GET` - `localhost:8080/games/` - return all games present in database
 - `GET` - `localhost:8080/games/{id}` - return game by id - the games ID will be present in a `POST` response or from the `GET` for all games
 - `PUT` - `localhost:8080/games/developer/{developer}/{id}` - update a game - requires id of game to be updated and developer of the game(can only update if developer matches what is present in the game)
 - `DELETE` - `localhost:8080/games/developer/{developer}/{id}` - delete a game - requires id of game to be deleted and developer of the game(can only delete if developer matches what is present in the game)
