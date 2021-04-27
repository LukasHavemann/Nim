package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.domain.service.GameNotFoundException
import de.havemann.lukas.nim.usecase.GameAlreadyFinished
import de.havemann.lukas.nim.usecase.UnknownMoveException
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Tests {@link PlayAgainstComputerUseCaseAdapter}
 */
@SpringBootTest
internal class PlayAgainstComputerUseCaseAdapterIntegrationTest {

    @Autowired
    lateinit var playAgainstComputerUseCaseAdapter: PlayAgainstComputerUseCaseAdapter

    @Test
    fun `play random game till the end`() {
        val initialGameState = playAgainstComputerUseCaseAdapter.createNimGame()
        val lastDraw = playGameUntilFinished(initialGameState.id)

        val softly = SoftAssertions()
        softly.assertThat(initialGameState.remainingMatches).isEqualTo(13)
        softly.assertThat(lastDraw.remainingMatches).isEqualTo(0)
        softly.assertThat(lastDraw.finished).isTrue
        softly.assertAll()
    }

    @Test
    fun `invalid gameId is handled gracefully`() {
        assertThrows<GameNotFoundException> { playAgainstComputerUseCaseAdapter.draw(Long.MAX_VALUE, "one") }
    }

    @Test
    fun `invalid move is handled gracefully`() {
        val createNimGame = playAgainstComputerUseCaseAdapter.createNimGame()
        assertThrows<UnknownMoveException> {
            playAgainstComputerUseCaseAdapter.draw(createNimGame.id, "abc")
        }
    }

    @Test
    fun `finished game is handled gracefully`() {
        val initialGameState = playAgainstComputerUseCaseAdapter.createNimGame()
        playGameUntilFinished(initialGameState.id)
        assertThrows<GameAlreadyFinished> { playAgainstComputerUseCaseAdapter.draw(initialGameState.id, "one") }
    }

    private fun playGameUntilFinished(gameId: Long): CurrentGameStateResponse {
        var afterDraw = playAgainstComputerUseCaseAdapter.draw(gameId, "one")
        while (!afterDraw.finished) {
            afterDraw = playAgainstComputerUseCaseAdapter.draw(gameId, "one")
        }
        return afterDraw
    }
}
