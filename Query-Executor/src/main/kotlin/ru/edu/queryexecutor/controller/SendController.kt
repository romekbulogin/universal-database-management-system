package ru.edu.queryexecutor.controller

import mu.KotlinLogging
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import ru.edu.queryexecutor.rabbit.producer.QueryProducer
import ru.edu.queryexecutor.request.FindDatabases
import ru.edu.queryexecutor.request.QueryRequest
import ru.edu.queryexecutor.service.DatabaseListViewService

@RestController
@RequestMapping("/api")
class SendController(
    private val queryProducer: QueryProducer,
    private val databaseListViewService: DatabaseListViewService
) {
    private val logger = KotlinLogging.logger { }

    @PostMapping("/send_query")
    fun sendQuery(@RequestBody request: QueryRequest): Any = queryProducer.sendQuery(request)

    @PostMapping("/user_databases")
    fun findAllDatabaseForUser(@RequestBody request: FindDatabases): Any? =
        databaseListViewService.findAllDatabasesForUser(request)
}