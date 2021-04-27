# :game_die: Nim Game

Simple implementation of the Nim Game in Kotlin. The nim game can be played via a REST Interface. To learn more about
how to play the game against the server, have a look
at [sampla game interactions](https://raw.githubusercontent.com/LukasHavemann/Nim/main/src/test/resources/sample-game-interaction.rest)
. If you are running IntelliJ IDE you can execute
them [directly from your IDE](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html).
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

## :house: Architecture

The architecture follows the hexagonal architecture
of [Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/).

## :hammer: Development, Build & Pipeline

The execution of unit and intests tests were automated
with [GitHub Actions](https://github.com/LukasHavemann/vanilla-http-server/actions). The docker image build is automated
with [DockerHub](https://hub.docker.com/repository/docker/lukashavemann/vanilla-http-server). As soon as a new merge to
master happens, a new docker image is built by DockerHub cloud and provided with latest tag in
the [DockerHub registry](https://hub.docker.com/repository/docker/lukashavemann/vanilla-http-server).

The docker image is built in a multi-stage docker build. The maven build process produces
a [layered jar](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/#repackage-layers).
During the build process the layered jar gets unpacked and added as separate layers, to make use of the docker layer
deduplication feature to reduce server startup time.
