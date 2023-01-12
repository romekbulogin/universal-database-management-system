package ru.edu.authorizationservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.authorizationservice.entity.RoleEntity
import ru.edu.authorizationservice.service.RoleService

@RestController
@RequestMapping("/api/role")
class RoleController(private val roleService: RoleService) {

    @PostMapping("/create")
    fun createRole(@RequestBody roleEntity: RoleEntity) = roleService.saveRole(roleEntity)

    @GetMapping("/find_all")
    fun findAll() = roleService.findRoles()
}