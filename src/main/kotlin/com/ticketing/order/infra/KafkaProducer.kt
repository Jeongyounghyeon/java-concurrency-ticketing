package com.ticketing.order.infra

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun send(topic: String, message: String) {
        kafkaTemplate.send(topic, message)
    }
}
