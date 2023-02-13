package ru.edu.authorizationservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.authorizationservice.feign.databasemanager.DatabaseManagerClient
import ru.edu.authorizationservice.request.AddDatabaseRequest
import ru.edu.authorizationservice.request.DeleteDatabase
import ru.edu.authorizationservice.service.DatabaseService

@RestController
@RequestMapping("/api/database")
class DatabaseController(
    private val databaseService: DatabaseService,
) {

    @PostMapping("/add_for_user")
    fun addDatabaseForUser(
        @RequestBody addDatabaseRequest: AddDatabaseRequest,
        @RequestHeader(value = "Authorization") token: String
    ) = databaseService.addNewDatabaseForUser(addDatabaseRequest, token)

    @PostMapping("/delete_database")
    fun deleteDatabase(
        @RequestBody deleteDatabase: DeleteDatabase,
        @RequestHeader(value = "Authorization") token: String
    ) = databaseService.deleteDeleteDatabase(deleteDatabase, token)

    @GetMapping("/my_databases")
    fun userDatabase(@RequestHeader(value = "Authorization") token: String) = databaseService.viewDatabaseList(token)

    @PostMapping("/auth_param")
    fun findLoginAndPassword(
        @RequestBody request: AddDatabaseRequest,
        @RequestHeader(value = "Authorization") token: String
    ) = databaseService.findLoginAndPassword(request, token)
}