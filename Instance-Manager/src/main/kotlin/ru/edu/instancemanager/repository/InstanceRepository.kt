package ru.edu.instancemanager.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edu.instancemanager.entity.InstanceEntity

@Repository
interface InstanceRepository : JpaRepository<InstanceEntity, Int> {
    fun findByDbms(dbms: String): InstanceEntity
}