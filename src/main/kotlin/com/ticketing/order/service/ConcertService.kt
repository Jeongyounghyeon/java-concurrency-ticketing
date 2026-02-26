package com.ticketing.order.service

import com.ticketing.order.dto.OrderRequest
import com.ticketing.order.infra.KafkaProducer
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class ConcertService(
    private val kafkaProducer: KafkaProducer,
    private val objectMapper: ObjectMapper
) {
    private val ORDER_TOPIC = "order-topic"

    fun requestOrder(concertId: Long, quantity: Int, userId: String) {
        val orderRequest = OrderRequest(concertId, quantity, userId)
        val message = objectMapper.writeValueAsString(orderRequest)
        kafkaProducer.send(ORDER_TOPIC, message)
    }
}
