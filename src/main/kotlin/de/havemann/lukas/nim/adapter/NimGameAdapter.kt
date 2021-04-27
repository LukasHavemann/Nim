package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.domain.service.NimGameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Manage Nim Games
 * REST APIs to view and manage created nim game instances
 */
@RestController
@RequestMapping("nimgame/")
class NimGameAdapter(@Autowired private val nimGameService: NimGameService) {

    @GetMapping(path = ["/{gameId}"])
    fun getCurrentState(@PathVariable gameId: Long): CurrentGameStateResponse {
        return CurrentGameStateResponse(nimGameService.findGame(gameId))
    }

    @DeleteMapping(path = ["/{gameId}"])
    fun deleteGame(@PathVariable gameId: Long): CurrentGameStateResponse {
        return CurrentGameStateResponse(nimGameService.deleteGame(gameId))
    }
}