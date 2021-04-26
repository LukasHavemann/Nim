package de.havemann.lukas.nim.domain.entities

import de.havemann.lukas.nim.domain.entites.NimGame
import de.havemann.lukas.nim.domain.entites.NimGameImpl
import de.havemann.lukas.nim.domain.valuetype.Match
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


/**
 * Testing game logic of {@link NimGame}
 */
internal class NimGameTest {

    private lateinit var softly: SoftAssertions
    private lateinit var nimGame: NimGame
    private lateinit var player1: MockablePlayer
    private lateinit var player2: MockablePlayer

    @BeforeEach
    private fun setup() {
        player2 = MockablePlayer(Match.ONE)
        player1 = MockablePlayer(Match.ONE)
        nimGame = NimGameImpl(1L, player1, player2, Match(13))
        softly = SoftAssertions()
    }

    @Test
    fun `game should have at least 2 matches`() {
        assertThrows<IllegalArgumentException> { NimGameImpl(1L, player1, player2, Match(0)) }
        assertThrows<IllegalArgumentException> { NimGameImpl(1L, player1, player2, Match(1)) }
    }

    @Test
    fun `simple game run with one match draw`() {
        softly.assertThat(nimGame.gameState).`as`("before start").isEqualTo(NimGame.GameState.NOT_STARTED)

        nimGame.start()

        softly.assertThat(nimGame.gameState).isEqualTo(NimGame.GameState.FINISHED)
        softly.assertThat(nimGame.currentMatches).isEqualTo(Match.ZERO)
        softly.assertThat(nimGame.winner).isEqualTo(player1)
        softly.assertAll()
    }

    @Test
    fun `more complex game run`() {
        player1.draws = mutableListOf(
            Match.THREE,  // remaining 10
            Match.THREE,  // remaining 4
            Match.ONE,    // remaining 2
            Match.ONE     // last draw. player 1 loses
        )

        player2.draws = mutableListOf(
            Match.THREE,  // remaining 7
            Match.ONE,    // remaining 3
            Match.ONE     // remaining 1
        )

        nimGame.start()
        softly.assertThat(nimGame.gameState).isEqualTo(NimGame.GameState.FINISHED)
        softly.assertThat(nimGame.currentMatches).isEqualTo(Match.ZERO)
        softly.assertThat(nimGame.winner).isEqualTo(player2)
        softly.assertAll()
    }

    @Test
    fun `zero draw not allowed`() {
        player1.nextMove = Match.ZERO
        assertThrows<IllegalStateException> { nimGame.start() }
    }

    @Test
    fun `bigger than three draw not allowed`() {
        player1.nextMove = Match(5)
        assertThrows<IllegalStateException> { nimGame.start() }
    }

    data class MockablePlayer(var nextMove: Match, override val name: String = "mocked player") : NimGame.Player {
        var draws: MutableList<Match> = Collections.emptyList()

        override fun requestToDraw(turn: NimGame.Turn) {
            if (draws.isEmpty()) {
                turn.draw(nextMove)
                return
            }

            turn.draw(draws.removeAt(0))
        }
    }
}