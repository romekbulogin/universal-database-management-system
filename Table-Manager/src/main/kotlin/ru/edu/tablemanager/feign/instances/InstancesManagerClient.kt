package ru.edu.tablemanager.feign.instances

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import ru.edu.tablemanager.feign.instances.request.InstanceEntity

@FeignClient(value = "instancesmanager", url = "http://localhost:8084/")
interface InstancesManagerClient {
    @PostMapping("/api/instances/find_by_dbms/{dbms}")
    fun findInstanceByDbms(
        @PathVariable dbms: String
    ): InstanceEntity

    @PostMapping("/api/instances/save_instance")
    fun saveInstance(@RequestBody instanceEntity: InstanceEntity): InstanceEntity
}