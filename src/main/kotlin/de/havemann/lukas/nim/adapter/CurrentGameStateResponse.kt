package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.domain.entites.NimGame

/**
 * DTO which represents most of the necessary information of {@link NimGame}
 */
data class CurrentGameStateResponse(
    val id: Long, val remainingMatches: Int, val finished: Boolean, val message: String
) {
    constructor(nimGame: NimGame) : this(
        nimGame.id,
        nimGame.currentMatches.value,
        nimGame.finished(),
        if (nimGame.finished())
            "game finished! The winner is " + nimGame.winner!!.name
        else
            nimGame.currentPlayer().name + " should draw!"
    )
}