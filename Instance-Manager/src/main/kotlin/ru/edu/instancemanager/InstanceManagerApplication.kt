package ru.edu.instancemanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InstanceManagerApplication

fun main(args: Array<String>) {
    runApplication<InstanceManagerApplication>(*args)
}
