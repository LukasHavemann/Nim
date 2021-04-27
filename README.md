# :game_die: Nim Game

Simple implementation of the Nim Game in Kotlin. The nim game can be played via a REST Interface. To learn more about
how to play the game against the server, have a look
at [sampla game interactions](src/test/resources/sample-game-interaction.rest). If you are running IntelliJJ you can
execute them [directly from your IDE](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html).
Alternatively you can start the server and go to `http://localhost:8080/swagger-ui/index.html`.

## :whale2: How to launch?

The nim game server is available as a docker image
on [DockerHub](https://hub.docker.com/repository/docker/lukashavemann/nim-game) and can be started with the following
command. After startup the REST API is available under `localhost:8080`.

```
docker run -d -p 8080:8080 lukashavemann/nim-game:latest
```

Alternatively you can check out the project and run:

````
mvn package
java -jar target/Nim-1.0-SNAPSHOT.jar
````