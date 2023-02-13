package ru.edu.tablemanager.feign.authentication

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.edu.tablemanager.feign.authentication.request.AuthParamRequest
import ru.edu.tablemanager.feign.authentication.response.AuthParamResponse
import ru.edu.tablemanager.feign.instances.request.InstanceEntity

@FeignClient(value = "authenticationservice", url = "http://localhost:8080/")
interface AuthenticationServiceClient {
    @PostMapping("/api/database/auth_param")
    fun getAuthParam(@RequestBody authParamRequest: AuthParamRequest): AuthParamResponse
}