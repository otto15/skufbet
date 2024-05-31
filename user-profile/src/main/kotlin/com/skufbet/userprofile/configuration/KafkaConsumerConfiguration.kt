package com.skufbet.userprofile.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.skufbet.userprofile.kafka.dto.BalanceOperationMessage
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
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfiguration {
    @Bean
    fun objectMapper(): ObjectMapper {
        return JacksonUtils.enhancedObjectMapper()
    }

    @Bean
    fun consumerFactory(
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
        @Value("\${spring.kafka.consumer.group-id}") groupId: String,
        @Value("\${spring.kafka.consumer.max-poll-records") maxPollResords: String,
        objectMapper: ObjectMapper,
    ): ConsumerFactory<String, BalanceOperationMessage> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        configProps[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = maxPollResords

        val kafkaConsumer = DefaultKafkaConsumerFactory<String, BalanceOperationMessage>(configProps)
        kafkaConsumer.setValueDeserializer(JsonDeserializer(objectMapper))

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