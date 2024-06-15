package com.skufbet.userprofile.kafka.error

import com.skufbet.userprofile.kafka.dto.BalanceOperationMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.serializer.FailedDeserializationInfo
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class DeserializationFailedFunction : Function<FailedDeserializationInfo, BalanceOperationMessage?> {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun apply(t: FailedDeserializationInfo): BalanceOperationMessage? {
        log.error("Error while deserialization. Message: ${String(t.data)}. Topic: ${t.topic}")
        return null
    }
}
