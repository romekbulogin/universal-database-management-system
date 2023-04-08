package ru.edu.querycreator.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.querycreator.rabbit.producer.QueryProducer
import ru.edu.querycreator.request.QueryRequest

@RestController
@RequestMapping("/api/query_executor")
class QueryCreatorController(
    private val queryProducer: QueryProducer,
) {

    @PostMapping("/send_query")
    fun sendQuery(@RequestBody request: QueryRequest): ResponseEntity<Any> = queryProducer.sendQuery(request)
}