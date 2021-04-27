package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.usecase.PlayAgainstComputerUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Play Against Computer Adapter
 * REST APIs to start and play nim games against a radom computer player
 **/
@RestController
@RequestMapping("nimgame/against/computer")
class PlayAgainstComputerUseCaseAdapter(
    @Autowired private val playAgainstComputerUseCase: PlayAgainstComputerUseCase
) {

    @Operation(summary = "Starts a new nim game instance")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun createNimGame(): CurrentGameStateResponse {
        val nimGame = playAgainstComputerUseCase.createGame("you")
        return CurrentGameStateResponse(nimGame)
    }

    @Operation(summary = "Executes a draw in the given nim game instance")
    @PutMapping(path = ["/{gameId}/draw"])
    @ResponseStatus(HttpStatus.OK)
    fun draw(
        @PathVariable gameId: Long,
        @Parameter(description = "the number of matches to draw", example = "one, two, three")
        @RequestParam count: String
    ): CurrentGameStateResponse {
        val allowedMove = PlayAgainstComputerUseCase.AllowedMove.fromString(count)
        return CurrentGameStateResponse(playAgainstComputerUseCase.draw(gameId, allowedMove))
    }
}