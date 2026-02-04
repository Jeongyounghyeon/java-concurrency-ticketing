package com.ticketing.order.service

import org.redisson.api.RedissonClient
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
@Profile("redis")
class DistributedLockConcertFacade(
    private val redissonClient: RedissonClient,
    private val concertService: ConcertService
) {

    fun decreaseSeats(concertId: Long, quantity: Int) {
        val lock = redissonClient.getLock("concert_lock:$concertId")

        try {
            // 락 획득 시도 (wait time: 10s, lease time: 3s)
            val available = lock.tryLock(10, 3, TimeUnit.SECONDS)

            if (!available) {
                println("Lock 획득 실패")
                return
            }

            concertService.decreaseSeats(concertId, quantity)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}
