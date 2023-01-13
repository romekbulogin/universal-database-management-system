package ru.edu.queryexecutor.rabbit.producer

import com.beust.klaxon.JsonArray
import com.beust.klaxon.Parser
import mu.KotlinLogging
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.request.QueryRequest


@Service
class QueryProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val bindingRequest: Binding,
    private val bindingResponse: Binding,
) {
    private val logger = KotlinLogging.logger { }

    @Value("\${spring.rabbitmq.consumer.response.queue-response}")
    private val queueResponseName: String? = null

    fun sendQuery(request: QueryRequest): Any {
        try {
            val props: MessageProperties =
                MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build()
            props.setHeader("database", request.database)
            props.setHeader("dbms", request.dbms)
            rabbitTemplate.send(
                bindingRequest.exchange,
                bindingRequest.routingKey,
                Message(request.sql.toByteArray(), props)
            )
            logger.info("Request for execute: ${request.sql}")
            val response = rabbitTemplate.receive(queueResponseName!!)

            return try {
                val json = Parser().parse(StringBuilder(response?.body!!.toString(Charsets.UTF_8))) as JsonArray<*>
                json.value
            } catch (ex: ClassCastException) {
                logger.error(ex.message)
                response?.body!!.toString(Charsets.UTF_8)
            } catch (ex: NullPointerException) {
                logger.error(ex.message)
                response?.body!!.toString(Charsets.UTF_8)
            }
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw ex
        }
    }

    fun sendQueryResponse(response: Any?) {
        try {
            rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
            rabbitTemplate.convertAndSend(bindingResponse.exchange, bindingResponse.routingKey, response!!)
            logger.info("Response: $response")
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw ex
        }
    }
}