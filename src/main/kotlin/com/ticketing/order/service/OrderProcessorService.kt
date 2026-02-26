package com.ticketing.order.service

import com.ticketing.order.domain.ConcertRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderProcessorService(
    private val concertRepository: ConcertRepository
) {
    @Transactional
    fun processOrder(concertId: Long, quantity: Int) {
        val concert = concertRepository.findByIdWithPessimisticLock(concertId)
            .orElseThrow { IllegalArgumentException("Concert not found") }

        if (concert.availableSeats < quantity) {
            throw IllegalArgumentException("Not enough seats available.")
        }

        concert.availableSeats -= quantity
    }
}
