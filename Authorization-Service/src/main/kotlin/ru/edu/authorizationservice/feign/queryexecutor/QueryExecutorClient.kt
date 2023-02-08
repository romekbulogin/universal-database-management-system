package ru.edu.authorizationservice.feign.queryexecutor

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.edu.authorizationservice.feign.queryexecutor.request.QueryRequest


@FeignClient(value = "queryexecutor", url = "http://localhost:8082/api/query_executor")
interface QueryExecutorClient {
    @PostMapping("/send_query")
    fun sendQuery(
        @RequestBody request: QueryRequest
    ): Any
}