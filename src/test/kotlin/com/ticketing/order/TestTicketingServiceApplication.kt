package com.ticketing.order

import com.ticketing.TicketingServiceApplication
import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<TicketingServiceApplication>().with(TestcontainersConfiguration::class).run(*args)
}
