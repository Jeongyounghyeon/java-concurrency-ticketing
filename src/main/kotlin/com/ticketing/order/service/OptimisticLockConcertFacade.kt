package com.ticketing.order.service

import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Component

@Component
class OptimisticLockConcertFacade(
    private val concertService: ConcertService
) {
    fun decreaseSeats(concertId: Long, quantity: Int) {
        while (true) {
            try {
                concertService.decreaseSeats(concertId, quantity)
                break
            } catch (e: ObjectOptimisticLockingFailureException) {
                try {
                    Thread.sleep(50) // 재시도 전 50ms 대기
                } catch (interruptedException: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw RuntimeException(interruptedException)
                }
            }
        }
    }
}
