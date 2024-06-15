package com.skufbet.userprofile.kafka

import com.skufbet.userprofile.kafka.dto.BalanceOperationMessage
import com.skufbet.userprofile.service.UserProfileBalanceService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class MessageConsumer(
    private val balanceService: UserProfileBalanceService
) {
    @KafkaListener(topics = ["deposit-topic"], groupId = "deposit-group")
    fun listen(@Payload message: List<BalanceOperationMessage?>) {
        log.info("Received message: $message")
        message.filterNotNull().forEach {
            balanceService.deposit(it.clientId, it.amount)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MessageConsumer::class.java)
    }
}
