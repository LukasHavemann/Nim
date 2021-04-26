package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.usecase.PlayAgainstComputerUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("nimgame/against/computer")
class PlayAgainstComputerUseCaseAdapter(
    @Autowired private val playAgainstComputerUseCase: PlayAgainstComputerUseCase
) {

    @PostMapping("/create")
    fun createNimGame(): CurrentGameStateResponse {
        val nimGame = playAgainstComputerUseCase.createGame("you")
        return CurrentGameStateResponse(nimGame)
    }

    @PutMapping(path = ["/{gameId}/draw"])
    fun draw(@PathVariable gameId: Long, @RequestParam count: String): CurrentGameStateResponse {
        val allowedMove = PlayAgainstComputerUseCase.AllowedMove.fromString(count)
        return CurrentGameStateResponse(playAgainstComputerUseCase.draw(gameId, allowedMove))
    }
}