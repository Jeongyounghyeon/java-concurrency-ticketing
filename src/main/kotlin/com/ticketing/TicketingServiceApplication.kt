package com.ticketing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TicketingServiceApplication

fun main(args: Array<String>) {
    runApplication<TicketingServiceApplication>(*args)
}
