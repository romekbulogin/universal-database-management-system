package ru.edu.authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServiceApplication>(*args)
}
