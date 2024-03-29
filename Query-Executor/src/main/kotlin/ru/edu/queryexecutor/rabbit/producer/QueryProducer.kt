package ru.edu.queryexecutor.rabbit.producer

import com.beust.klaxon.JsonArray
import com.beust.klaxon.KlaxonException
import com.beust.klaxon.Parser
import mu.KotlinLogging
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.core.MessagePropertiesBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.RemoteInvocationResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.request.QueryRequest
import java.lang.ClassCastException
import java.sql.SQLException


@Service
class QueryProducer(
    private val rabbitTemplate: RabbitTemplate,
    private val bindingRequest: Binding,
) {
    private val logger = KotlinLogging.logger { }
    fun sendQuery(request: QueryRequest): ResponseEntity<Any> {
        return try {
            val props: MessageProperties =
                MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build()
            props.setHeader("database", request.database)
            props.setHeader("dbms", request.dbms)
            props.setHeader("login", request.login)
            props.setHeader("password", request.password)
            val response = rabbitTemplate.convertSendAndReceive(
                bindingRequest.exchange,
                bindingRequest.routingKey,
                Message(request.sql.toByteArray(), props)
            )
            logger.debug("Response: $response")
            ResponseEntity(response, HttpStatus.OK)
        } catch (ex: Exception) {
            logger.error(ex.message)
            ResponseEntity(mapOf("error" to ex.message), HttpStatus.BAD_REQUEST)
        }
    }
}