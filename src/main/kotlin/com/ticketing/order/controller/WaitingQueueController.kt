package com.ticketing.order.controller

import com.ticketing.order.service.WaitingQueueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/queue")
class WaitingQueueController(
    private val waitingQueueService: WaitingQueueService
) {

    @PostMapping("/tokens")
    fun register(@RequestParam userId: String): Long {
        return waitingQueueService.register(userId)
    }

    @GetMapping("/tokens/status")
    fun getStatus(@RequestParam userId: String): String {
        if (waitingQueueService.isAllowed(userId)) {
            return "ALLOWED"
        }
        val rank = waitingQueueService.getRank(userId)
        return if (rank >= 0) "WAITING (Rank: $rank)" else "NOT_FOUND"
    }
}
