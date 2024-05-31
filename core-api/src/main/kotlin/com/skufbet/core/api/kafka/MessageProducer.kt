package com.skufbet.core.api.kafka

import com.skufbet.core.api.kafka.dto.BalanceOperationMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class MessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, BalanceOperationMessage>
) {
    fun sendMessage(
        topic: String = "skufbet-topic",
        message: BalanceOperationMessage
    ) {
        kafkaTemplate.send(topic, message)
    }
}