package com.skufbet.userprofile.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.skufbet.userprofile.kafka.dto.BalanceOperationMessage
import com.skufbet.userprofile.kafka.error.DeserializationFailedFunction
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.JacksonUtils
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfiguration {
    @Bean
    fun objectMapper(): ObjectMapper {
        return JacksonUtils.enhancedObjectMapper().configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
    }

    @Bean
    fun consumerFactory(
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
        @Value("\${spring.kafka.consumer.group-id}") groupId: String,
        @Value("\${spring.kafka.consumer.max-poll-records}") maxPollRecords: String,
        objectMapper: ObjectMapper,
        deserializationFailedFunction: DeserializationFailedFunction
    ): ConsumerFactory<String, BalanceOperationMessage> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = ErrorHandlingDeserializer::class.java
        configProps[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = maxPollRecords
        configProps[JsonDeserializer.TRUSTED_PACKAGES] = "com.skufbet.core.api.kafka.dto"
        configProps[JsonDeserializer.USE_TYPE_INFO_HEADERS] = false
        configProps[JsonDeserializer.VALUE_DEFAULT_TYPE] = "com.skufbet.userprofile.kafka.dto.BalanceOperationMessage"
        configProps[ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS] = JsonDeserializer::class.java
        configProps[ErrorHandlingDeserializer.VALUE_FUNCTION] = DeserializationFailedFunction::class.java

        val kafkaConsumer = DefaultKafkaConsumerFactory<String, BalanceOperationMessage>(configProps)
        val errorHandlingDeserializer = ErrorHandlingDeserializer<BalanceOperationMessage>(JsonDeserializer(objectMapper))
        errorHandlingDeserializer.setFailedDeserializationFunction(deserializationFailedFunction)

        kafkaConsumer.setValueDeserializer(errorHandlingDeserializer)

        return kafkaConsumer
    }

    @Bean
    fun kafkaListenerContainerFactory(
        @Qualifier("consumerFactory") consumerFactory: ConsumerFactory<String, BalanceOperationMessage>
    ): ConcurrentKafkaListenerContainerFactory<String, BalanceOperationMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, BalanceOperationMessage>()
        factory.consumerFactory = consumerFactory
        factory.isBatchListener = true
        return factory
    }
}