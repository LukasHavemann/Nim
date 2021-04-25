package de.havemann.lukas.nim.domain

import de.havemann.lukas.nim.base.Loggable
import de.havemann.lukas.nim.base.logger
import de.havemann.lukas.nim.domain.NimGame.Turn

/**
 * Representing the misère variant of the nim game
 */
interface NimGame {

    /**
     * Launches the game and asks the first player to draw
     */
    fun start()

    /**
     * @return describes the current game state
     */
    fun currentGameState(): GameState

    /**
     * @return Remaining matches in game
     */
    fun currentMatches(): Match

    /**
     * @return the winner of the game, if the game has finished
     */
    fun getWinner(): Player?

    /**
     * Representing a request to a player to execute a game move
     */
    interface Turn {

        /**
         * @return remaining matches in the game
         */
        fun remainingMatches(): Match

        /**
         * Executes the move by the player. The Player is allowed to draw between 1 and 3 matches
         */
        fun draw(toDraw: Match)
    }

    /**
     * Paricipant of the Nim game
     */
    interface Player {
        fun requestToDraw(turn: Turn)
    }

    enum class GameState {
        NOT_STARTED, PLAYER_1, PLAYER_2, FINISHED;

        fun otherPlayer(): GameState {
            require(this != NOT_STARTED && this != FINISHED)
            return if (this == PLAYER_1) PLAYER_2 else PLAYER_1
        }
    }
}

data class NimGameImpl(
    val player1: NimGame.Player,
    val player2: NimGame.Player,
    val initalMatches: Match
) : NimGame, Loggable {

    private var currentMatches: Match = initalMatches
    private var gameState: NimGame.GameState = NimGame.GameState.NOT_STARTED
    private var winner: NimGame.Player? = null

    init {
        require(initalMatches > Match.ONE)
    }

    override fun start() {
        require(gameState == NimGame.GameState.NOT_STARTED)
        this.gameState = NimGame.GameState.PLAYER_1
        currentPlayer().requestToDraw(TurnCallback())
    }

    override fun currentGameState(): NimGame.GameState {
        return gameState
    }

    override fun currentMatches(): Match {
        return currentMatches
    }

    override fun getWinner(): NimGame.Player? {
        return winner
    }

    fun executeDraw(toDraw: Match) {
        if (toDraw > currentMatches || !(Match.ONE..Match.THREE).contains(toDraw)) {
            throw IllegalStateException("Not allowed move $currentMatches $toDraw")
        }

        currentMatches -= toDraw
        gameState = gameState.otherPlayer()

        if (logger().isDebugEnabled) logger().debug(this.toString())

        if (!checkGameFinished()) {
            currentPlayer().requestToDraw(TurnCallback())
        }
    }

    private fun currentPlayer(): NimGame.Player {
        return if (gameState == NimGame.GameState.PLAYER_1) player1 else player2
    }

    private fun checkGameFinished(): Boolean {
        if (currentMatches == Match.ZERO && this.gameState != NimGame.GameState.FINISHED) {
            this.winner = currentPlayer()
            this.gameState = NimGame.GameState.FINISHED
        }
        return this.gameState == NimGame.GameState.FINISHED
    }

    override fun toString(): String {
        return "NimGameImpl(player1=$player1, player2=$player2, initalMatches=$initalMatches, currentMatches=$currentMatches, gameState=$gameState, winner=$winner)"
    }


    inner class TurnCallback(private var calledOnce: Boolean = false) : Turn {
        override fun remainingMatches(): Match {
            return currentMatches
        }

        override fun draw(toDraw: Match) {
            if (calledOnce) {
                throw IllegalStateException("already called")
            }
            calledOnce = true
            executeDraw(toDraw)
        }
    }
}


