package com.ticketing.order.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.ticketing.order.dto.OrderRequest
import com.ticketing.order.service.OrderProcessorService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    private val orderProcessorService: OrderProcessorService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(
        topics = ["order-topic"],
        groupId = "ticketing-group"
    )
    fun consume(message: String) {
        try {
            val orderRequest = objectMapper.readValue(message, OrderRequest::class.java)
            orderProcessorService.processOrder(orderRequest.concertId, orderRequest.quantity)
            
            println("Successfully processed order for user ${orderRequest.userId}")
        } catch (e: Exception) {
            println("Failed to process order: $message, error: ${e.message}")
        }
    }
}
