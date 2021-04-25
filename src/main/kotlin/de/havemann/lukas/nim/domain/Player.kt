package de.havemann.lukas.nim.domain

import kotlin.random.Random

/**
 * Player that draws automatically a random determined count of matches
 */
class RandomPlayer : NimGame.Player {
    override fun requestToDraw(turn: NimGame.Turn) {
        turn.draw(determineRandomValue(turn))
    }

    private fun determineRandomValue(turn: NimGame.Turn): Match {
        val maxValue = Match.min(Match.THREE, turn.remainingMatches())
        if (maxValue == Match.ONE) {
            return Match.ONE
        }

        return Match(Random.nextInt(Match.ONE.value, maxValue.value))
    }
}

/**
 * Player that remembers the current turn and waits for async invocation of turn
 */
class StatefulPlayer : NimGame.Player {

    private var currentTurn: NimGame.Turn? = null

    override fun requestToDraw(turn: NimGame.Turn) {
        currentTurn = turn
    }
}
