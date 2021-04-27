package de.havemann.lukas.nim.domain.service

import de.havemann.lukas.nim.domain.entites.NimGame
import de.havemann.lukas.nim.domain.entites.NimGameImpl
import de.havemann.lukas.nim.domain.valuetype.Match
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong
import javax.validation.constraints.Min


/**
 * Service for management of {@link NimGame}
 */
interface NimGameService {
    fun createNewGame(player1: NimGame.Player, player2: NimGame.Player): NimGame
    fun deleteGame(id: Long): NimGame
    fun findGame(gameId: Long): NimGame
}

@Service
class NimGameServiceInMemory : NimGameService {

    @Min(value = 2)
    @Value("\${de.havemann.lukas.nim.game.defaultMatches}")
    private var defaultMatchCount: Int = 0

    private val uniqueGameId = AtomicLong()
    private val games: ConcurrentMap<Long, NimGame> = ConcurrentHashMap()

    override fun createNewGame(player1: NimGame.Player, player2: NimGame.Player): NimGame {
        val gameId = uniqueGameId.incrementAndGet()
        val game = NimGameImpl(gameId, player1, player2, Match(defaultMatchCount))
        games[gameId] = game
        return game
    }

    override fun deleteGame(id: Long): NimGame {
        return games.remove(id) ?: throw GameNotFoundException(id)
    }

    override fun findGame(gameId: Long): NimGame {
        return games[gameId] ?: throw GameNotFoundException(gameId)
    }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class GameNotFoundException(gameId: Long) : RuntimeException("game not found gameId=$gameId")
