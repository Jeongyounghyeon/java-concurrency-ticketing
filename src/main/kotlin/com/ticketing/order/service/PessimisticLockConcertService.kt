package com.ticketing.order.service

import com.ticketing.order.domain.ConcertRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PessimisticLockConcertService(
    private val concertRepository: ConcertRepository
) {

    @Transactional
    fun decreaseSeats(concertId: Long, quantity: Int) {
        val concert = concertRepository.findByIdWithPessimisticLock(concertId)
            .orElseThrow { IllegalArgumentException("Concert not found") }
        concert.availableSeats -= quantity
    }
}
