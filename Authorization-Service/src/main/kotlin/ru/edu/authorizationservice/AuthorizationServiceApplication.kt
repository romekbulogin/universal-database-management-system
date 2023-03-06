package ru.edu.authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class AuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServiceApplication>(*args)
}