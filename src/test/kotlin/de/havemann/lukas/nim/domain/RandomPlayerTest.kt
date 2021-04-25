package de.havemann.lukas.nim.domain

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

internal class RandomPlayerTest {
    @Test
    fun `simple game with two random player`() {
        val softly = SoftAssertions()

        val nimGame = NimGameImpl(RandomPlayer(), RandomPlayer(), Match(13))
        nimGame.start()

        softly.assertThat(nimGame.currentGameState()).isEqualTo(NimGame.GameState.FINISHED)
        softly.assertThat(nimGame.currentMatches()).isEqualTo(Match.ZERO)
        softly.assertAll()
    }
}