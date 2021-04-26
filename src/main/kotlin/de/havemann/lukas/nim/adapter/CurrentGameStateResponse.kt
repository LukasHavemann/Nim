package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.domain.entites.NimGame

data class CurrentGameStateResponse(
    val id: Long, val remainingMatches: Int, val message: String
) {
    constructor(nimGame: NimGame) : this(
        nimGame.id,
        nimGame.currentMatches.value,
        if (nimGame.finished())
            "game finished! The winner is " + nimGame.winner!!.name
        else
            nimGame.currentPlayer().name + " should draw!"
    )
}