package de.havemann.lukas.nim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class NimServer

fun main(args: Array<String>) {
    runApplication<NimServer>(*args)
}