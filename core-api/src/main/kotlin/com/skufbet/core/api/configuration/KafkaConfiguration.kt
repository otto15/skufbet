package com.skufbet.core.api.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.std.StringSerializer
import com.skufbet.core.api.bet.dto.kafka.FinishTo
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.JacksonUtils
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@EnableKafka
@Configuration
class KafkaConfiguration(
    @Value(value = "\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value(value = "\${spring.kafka.topic") private val topicName: String,
    @Value(value = "\${spring.kafka.consumer.group-id") private val groupId: String,
) {
    @Bean
    fun objectMapper(): ObjectMapper {
        return JacksonUtils.enhancedObjectMapper()
    }

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val config = HashMap<String, Any>()
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        return KafkaAdmin(config)
    }

    @Bean
    fun topic() = TopicBuilder.name(topicName).partitions(2).replicas(2).build()

    @Bean
    fun producerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ProducerFactory<String, FinishTo> {
        val props = HashMap<String, Any>()

        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ProducerConfig.CLIENT_ID_CONFIG] = topicName
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        val kafkaProducer = DefaultKafkaProducerFactory<String, FinishTo>(props)
        kafkaProducer.valueSerializer = JsonSerializer(objectMapper)
        return kafkaProducer
    }

    @Bean
    fun kafkaTemplate(
        producerFactory: ProducerFactory<String, FinishTo>,
    ) = KafkaTemplate(producerFactory)

    @Bean
    fun consumerFactory(): ConsumerFactory<String, FinishTo> {
        val props = HashMap<String, Any>()

        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java


        val kafkaConsumer = DefaultKafkaConsumerFactory<String, FinishTo>(props)
        kafkaConsumer.setValueDeserializer(JsonDeserializer(objectMapper()))
        return kafkaConsumer
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, FinishTo>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, FinishTo>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}