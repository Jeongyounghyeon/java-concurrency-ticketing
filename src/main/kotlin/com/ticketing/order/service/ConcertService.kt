package com.ticketing.order.service

import com.ticketing.order.domain.ConcertRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConcertService(
    private val concertRepository: ConcertRepository
) {

    @Transactional
    fun decreaseSeats(concertId: Long, quantity: Int) {
        val concert = concertRepository.findById(concertId).orElseThrow { IllegalArgumentException("Concert not found") }
        concert.availableSeats -= quantity
    }
}
