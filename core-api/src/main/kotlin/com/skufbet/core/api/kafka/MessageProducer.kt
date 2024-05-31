package com.skufbet.core.api.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class MessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun sendMessage(topic: String, message: String) {
        kafkaTemplate.send(topic, message)
    }
}