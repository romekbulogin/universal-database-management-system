package ru.edu.queryexecutor.config

import com.rabbitmq.client.AMQP.Exchange
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {
    @Value("\${spring.rabbitmq.consumer.exchange}")
    private val exchange: String? = null

    @Value("\${spring.rabbitmq.consumer.queue}")
    private val queue: String? = null

    @Value("\${spring.rabbitmq.consumer.routing-key}")
    private val routingKey: String? = null

    @Bean
    fun queueExecutor() = Queue(queue, false)

    @Bean
    fun exchange() = DirectExchange(exchange)

    @Bean
    fun binding(): Binding? = BindingBuilder.bind(queueExecutor()).to(exchange()).with(routingKey)
}