package de.havemann.lukas.nim.domain.entites

import de.havemann.lukas.nim.base.Loggable
import de.havemann.lukas.nim.base.logger
import de.havemann.lukas.nim.domain.valuetype.Match

/**
 * Representing the misère variant of the nim game
 */
interface NimGame {

    val id: Long
    val initialMatches: Match
    val currentMatches: Match
    val gameState: GameState

    /**
     * @return winner of the game if {@link #gameState} is finished
     */
    val winner: Player?

    /**
     * Launches the game and asks the first player to draw
     */
    fun start()


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
     * Participant of the Nim game
     */
    interface Player {

        /**
         * Unique name identifying the player
         */
        val name: String

        fun requestToDraw(turn: Turn)
    }

    enum class GameState {
        NOT_STARTED, PLAYER_1, PLAYER_2, FINISHED;

        fun otherPlayer(): GameState {
            require(this != NOT_STARTED && this != FINISHED)
            return if (this == PLAYER_1) PLAYER_2 else PLAYER_1
        }
    }

    fun currentPlayer(): Player
    fun finished(): Boolean {
        return gameState == GameState.FINISHED
    }
}

data class NimGameImpl(
    override val id: Long,
    val player1: NimGame.Player,
    val player2: NimGame.Player,
    override val initialMatches: Match
) : NimGame, Loggable {

    init {
        require(initialMatches > Match.ONE)
    }

    override var currentMatches: Match = initialMatches
        private set
    override var gameState: NimGame.GameState = NimGame.GameState.NOT_STARTED
        private set
    override var winner: NimGame.Player? = null
        private set

    override fun start() {
        require(gameState == NimGame.GameState.NOT_STARTED)
        this.gameState = NimGame.GameState.PLAYER_1
        currentPlayer().requestToDraw(TurnCallback())
    }

    fun executeDraw(toDraw: Match) {
        if (canBeDrawn(toDraw)) {
            throw IllegalStateException("Not allowed move $currentMatches $toDraw")
        }

        currentMatches -= toDraw
        gameState = gameState.otherPlayer()

        if (logger().isDebugEnabled) logger().debug(this.toString())

        if (!checkGameFinished()) {
            currentPlayer().requestToDraw(TurnCallback())
        }
    }

    private fun canBeDrawn(toDraw: Match) =
        toDraw > currentMatches || !(Match.ONE..Match.THREE).contains(toDraw)

    override fun currentPlayer(): NimGame.Player {
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
        return "NimGameImpl(player1=$player1, " +
                "player2=$player2, " +
                "initialMatches=$initialMatches, " +
                "currentMatches=$currentMatches, " +
                "gameState=$gameState, " +
                "winner=$winner)"
    }

    inner class TurnCallback(private var calledOnce: Boolean = false) : NimGame.Turn {
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