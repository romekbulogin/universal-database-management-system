package ru.edu.queryexecutor.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfiguration {
    @Value("\${spring.rabbitmq.consumer.exchange}")
    private val exchange: String? = null

    @Value("\${spring.rabbitmq.consumer.request.queue}")
    private val queueRequestName: String? = null

    @Value("\${spring.rabbitmq.consumer.response.queue-response}")
    private val queueResponseName: String? = null

    @Value("\${spring.rabbitmq.consumer.request.routing-key}")
    private val routingKeyRequest: String? = null

    @Value("\${spring.rabbitmq.consumer.response.routing-key-response}")
    private val routingKeyResponse: String? = null


    @Bean
    fun queueExecutor() = Queue(queueRequestName, false)

    @Bean
    fun queueResponse() = Queue(queueResponseName, false)

    @Bean
    fun exchange() = DirectExchange(exchange)

    @Bean
    fun bindingRequest(): Binding? = BindingBuilder.bind(queueExecutor()).to(exchange()).with(routingKeyRequest)

    @Bean
    fun bindingResponse(): Binding? = BindingBuilder.bind(queueResponse()).to(exchange()).with(routingKeyResponse)
}