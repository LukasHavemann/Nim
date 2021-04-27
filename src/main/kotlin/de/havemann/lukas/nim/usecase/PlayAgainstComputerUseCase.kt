package de.havemann.lukas.nim.usecase

import de.havemann.lukas.nim.domain.entites.NimGame
import de.havemann.lukas.nim.domain.entites.RandomPlayer
import de.havemann.lukas.nim.domain.entites.StatefulPlayer
import de.havemann.lukas.nim.domain.service.NimGameService
import de.havemann.lukas.nim.domain.valuetype.Match
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Implements a simple use case where human player can play against a random computer player
 */
@Service
class PlayAgainstComputerUseCase(@Autowired private val nimGameService: NimGameService) {

    fun createGame(playerName: String): NimGame {
        val statefulPlayer = StatefulPlayer(playerName)
        val game = nimGameService.createNewGame(statefulPlayer, RandomPlayer())
        game.start()
        return game
    }

    fun draw(gameId: Long, move: AllowedMove): NimGame {
        val foundGame = nimGameService.findGame(gameId)
        if (foundGame.finished()) {
            throw GameAlreadyFinished(gameId)
        }

        // if we play against the computer, we no that current player should be the human
        (foundGame.currentPlayer() as StatefulPlayer).draw(move.toMatches)
        return foundGame
    }

    enum class AllowedMove(val toMatches: Match) {
        ONE(Match.ONE), TWO(Match.TWO), THREE(Match.THREE);

        companion object {
            fun fromString(value: String): AllowedMove {
                for (enumValue in values()) {
                    val normalized = value.toLowerCase().trim()
                    if (enumValue.name.toLowerCase() == normalized) {
                        return enumValue
                    }
                }

                throw UnknownMoveException(value)
            }
        }
    }
}

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class UnknownMoveException(got: String) : RuntimeException("Unknown move $got")

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class GameAlreadyFinished(gameId: Long) : RuntimeException("Game already finished $gameId")
