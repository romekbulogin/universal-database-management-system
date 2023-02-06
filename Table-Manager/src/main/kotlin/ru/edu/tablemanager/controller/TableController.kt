package ru.edu.tablemanager.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.edu.tablemanager.request.TableViewRequest
import ru.edu.tablemanager.service.TableManagerService

@RestController
@RequestMapping("/api/table_manager")
class TableController(private val tableManagerService: TableManagerService) {

    @PostMapping("/view")
    fun viewTable(request: TableViewRequest) = tableManagerService.viewTable(request)
}