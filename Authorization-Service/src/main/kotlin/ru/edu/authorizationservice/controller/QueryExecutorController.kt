package ru.edu.authorizationservice.controller

import feign.Headers
import org.springframework.web.bind.annotation.*
import ru.edu.authorizationservice.feign.queryexecutor.QueryExecutorClient
import ru.edu.authorizationservice.feign.queryexecutor.request.QueryRequest
import ru.edu.authorizationservice.request.QueryRequestAuth
import ru.edu.authorizationservice.service.QueryExecutorService

@RestController
@RequestMapping("/api/query_executor")
class QueryExecutorController(private val queryExecutorService: QueryExecutorService) {

    @PostMapping("/send_query")
    fun sendQuery(@RequestBody request: QueryRequestAuth, @RequestHeader(value = "Authorization") token: String) =
        queryExecutorService.sendQuery(request, token)

}