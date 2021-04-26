package de.havemann.lukas.nim.adapter

import de.havemann.lukas.nim.domain.service.NimGameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("nimgame/")
class NimGameAdapter(@Autowired private val nimGameService: NimGameService) {

    @GetMapping(path = ["/{gameId}"])
    fun getCurrentState(@PathVariable gameId: Long): CurrentGameStateResponse {
        return CurrentGameStateResponse(nimGameService.findGame(gameId))
    }

}