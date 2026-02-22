package com.ticketing.order.controller

import com.ticketing.order.service.DistributedLockConcertFacade
import com.ticketing.order.service.WaitingQueueService
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/concerts")
@Profile("redis")
class ConcertController(
    private val distributedLockConcertFacade: DistributedLockConcertFacade,
    private val waitingQueueService: WaitingQueueService
) {

    @PostMapping("/{id}/decrease-seats")
    fun decreaseSeats(
        @PathVariable id: Long,
        @RequestParam quantity: Int,
        @RequestParam userId: String
    ) {
        if (!waitingQueueService.isAllowed(userId)) {
            throw IllegalStateException("Access denied. Please wait in the queue.")
        }
        distributedLockConcertFacade.decreaseSeats(id, quantity)
    }
}
