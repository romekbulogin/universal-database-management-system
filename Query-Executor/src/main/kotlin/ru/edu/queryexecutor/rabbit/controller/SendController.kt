package ru.edu.queryexecutor.rabbit.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.edu.queryexecutor.rabbit.producer.QueryProducer
import ru.edu.queryexecutor.request.QueryRequest

@RestController
@RequestMapping("/api")
class SendController(private val queryProducer: QueryProducer) {

    @PostMapping("/send_query")
    fun sendQuery(@RequestBody request: QueryRequest) = queryProducer.sendQuery(request)
}