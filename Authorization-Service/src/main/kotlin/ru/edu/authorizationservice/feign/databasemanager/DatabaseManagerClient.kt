package ru.edu.authorizationservice.feign.databasemanager

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.edu.authorizationservice.feign.databasemanager.response.createddatabase.SuccessfullyCreateDatabase
import ru.edu.authorizationservice.feign.databasemanager.response.deleteddatabase.SuccessfullyDeleteDatabase
import ru.edu.authorizationservice.request.AddDatabaseRequest
import ru.edu.authorizationservice.request.DeleteDatabase

@FeignClient(value = "databasemanager", url = "http://localhost:8081/")
interface DatabaseManagerClient {

    @PostMapping("/api/create_database")
    fun createDatabase(
        @RequestBody addDatabaseRequest: AddDatabaseRequest
    ): SuccessfullyCreateDatabase

    @PostMapping("/api/delete_database")
    fun deleteDatabase(deleteRequest: DeleteDatabase): SuccessfullyDeleteDatabase
}