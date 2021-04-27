package de.havemann.lukas.nim.domain.entites

import de.havemann.lukas.nim.domain.valuetype.Match
import kotlin.random.Random

/**
 * Player that draws automatically a random determined count of matches
 */
public class RandomPlayer(override val name: String = "random computer player") : NimGame.Player {
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

    override fun toString(): String {
        return "RandomPlayer(name='$name')"
    }
}

/**
 * Player that remembers the current turn and waits for async invocation of turn
 */
class StatefulPlayer(override val name: String) : NimGame.Player {

    private var currentTurn: NimGame.Turn? = null

    fun draw(match: Match) {
        synchronized(this) {
            currentTurn?.draw(match)
        }
    }

    override fun requestToDraw(turn: NimGame.Turn) {
        currentTurn = turn
    }

    override fun toString(): String {
        return "StatefulPlayer(name='$name')"
    }
}
