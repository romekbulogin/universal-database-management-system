package ru.edu.authorizationservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.authorizationservice.entity.UsersEntity
import ru.edu.authorizationservice.service.UserService

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @PostMapping("/registration")
    fun registration(@RequestBody usersEntity: UsersEntity) = userService.saveUser(usersEntity)
    @GetMapping("/find_all")
    fun getUsers() = userService.getUsers()
}