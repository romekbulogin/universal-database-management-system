package ru.edu.queryexecutor.rabbit.consumer

import mu.KotlinLogging
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import ru.edu.queryexecutor.request.QueryRequest

@Service
class QueryConsumer {
    private val logger = KotlinLogging.logger { }

    @RabbitListener(queues = ["\${spring.rabbitmq.consumer.queue}"])
    fun queryHandler(message: Message<String>) {
        logger.info(message.toString())
    }

    fun executeQuery(request: QueryRequest): MutableList<MutableMap<String, Any?>>? {
        return null
    }
}