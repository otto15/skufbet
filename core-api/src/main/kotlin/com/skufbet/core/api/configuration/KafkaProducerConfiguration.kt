package com.skufbet.core.api.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.skufbet.core.api.kafka.dto.BalanceOperationMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.JacksonUtils
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
class KafkaProducerConfiguration {
    @Bean
    fun objectMapper(): ObjectMapper {
        return JacksonUtils.enhancedObjectMapper()
    }

    @Bean
    fun producerFactory(
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
        objectMapper: ObjectMapper,
    ): ProducerFactory<String, BalanceOperationMessage> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        val kafkaProducer = DefaultKafkaProducerFactory<String, BalanceOperationMessage>(configProps)
        kafkaProducer.valueSerializer = JsonSerializer(objectMapper)
        return kafkaProducer
    }

    @Bean
    fun kafkaTemplate(
        @Qualifier("producerFactory") producerFactory: ProducerFactory<String, BalanceOperationMessage>
    ): KafkaTemplate<String, BalanceOperationMessage> = KafkaTemplate(producerFactory)
}