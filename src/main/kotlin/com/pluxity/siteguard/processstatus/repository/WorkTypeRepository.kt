package com.pluxity.siteguard.processstatus.repository

import com.pluxity.siteguard.processstatus.entity.WorkType
import org.springframework.data.jpa.repository.JpaRepository

interface WorkTypeRepository : JpaRepository<WorkType, Long> {
    fun existsByName(name: String): Boolean
}
