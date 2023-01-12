package ru.edu.queryexecutor.rabbit.producer

import mu.KotlinLogging
import org.apache.coyote.Response
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.request.QueryRequest


@Service
class QueryProducer(private val rabbitTemplate: RabbitTemplate, private val exchange: DirectExchange, private val binding: Binding) {
    private val logger = KotlinLogging.logger { }
    fun sendQuery(request: QueryRequest) {
        try {
            val props: MessageProperties = MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.DEFAULT_CONTENT_TYPE).build()
            props.setHeader("database", request.database)
            props.setHeader("dbms", request.dbms)
            rabbitTemplate.send(exchange.name, binding.routingKey, Message(request.sql.toByteArray(), props))
            logger.info("Request for execute: ${request.sql}")
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw ex
        }
    }
}