package ru.edu.authorizationservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.edu.authorizationservice.entity.DatabaseEntity

@Repository
interface DatabaseRepository : JpaRepository<DatabaseEntity, Int> {}