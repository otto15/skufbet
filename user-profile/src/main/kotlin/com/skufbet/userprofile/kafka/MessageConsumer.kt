package com.skufbet.userprofile.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MessageConsumer {
    @KafkaListener(topics = ["skufbet"], groupId = "deposit-group")
    fun listen(message: String) {
        log.error("Received message: $message")
    }

    companion object {
        private val log = LoggerFactory.getLogger(MessageConsumer::class.java)
    }
}
