package ru.edu.queryexecutor.rabbit.consumer

import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener

class QueryListener {
    private val logger = KotlinLogging.logger { }

    @RabbitListener(queues = ["query-executor.in.queue"])
    fun queryHandler(message: String) = logger.info { message }
}