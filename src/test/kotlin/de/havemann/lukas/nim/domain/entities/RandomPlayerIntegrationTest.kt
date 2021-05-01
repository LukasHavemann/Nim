package de.havemann.lukas.nim.domain.entities

import de.havemann.lukas.nim.domain.entites.NimGame
import de.havemann.lukas.nim.domain.entites.NimGameImpl
import de.havemann.lukas.nim.domain.entites.RandomPlayer
import de.havemann.lukas.nim.domain.valuetype.Match
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

internal class RandomPlayerIntegrationTest {
    @Test
    fun `simple game with two random player`() {
        val nimGame = NimGameImpl(1L, RandomPlayer(), RandomPlayer(), Match(13))
        nimGame.start()

        SoftAssertions().apply {
            assertThat(nimGame.gameState).isEqualTo(NimGame.GameState.FINISHED)
            assertThat(nimGame.currentMatches).isEqualTo(Match.ZERO)
        }.assertAll()
    }
}