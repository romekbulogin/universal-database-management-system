package ru.edu.authorizationservice.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.authorizationservice.request.AddDatabaseRequest
import ru.edu.authorizationservice.service.DatabaseService

@RestController
@RequestMapping("/api/database")
class DatabaseController(private val databaseService: DatabaseService) {

    @PostMapping("/add_for_user")
    fun addDatabaseForUser(
        @RequestBody addDatabaseRequest: AddDatabaseRequest,
        @RequestHeader(value = "Authorization") token: String
    ) =
        databaseService.addNewDatabaseForUser(addDatabaseRequest, token)
}