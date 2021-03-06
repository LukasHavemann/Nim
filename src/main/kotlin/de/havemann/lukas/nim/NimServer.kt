package de.havemann.lukas.nim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
open class NimServer

fun main(args: Array<String>) {
    runApplication<NimServer>(*args)
}