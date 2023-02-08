package ru.edu.queryexecutor.controller


import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.queryexecutor.rabbit.producer.QueryProducer
import ru.edu.queryexecutor.request.QueryRequest

@RestController
@RequestMapping("/api/query_executor")
class SendController(
    private val queryProducer: QueryProducer,
) {

    @PostMapping("/send_query")
    fun sendQuery(@RequestBody request: QueryRequest): Any = queryProducer.sendQuery(request)
}