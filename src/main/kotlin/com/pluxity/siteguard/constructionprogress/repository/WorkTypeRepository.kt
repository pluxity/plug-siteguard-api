package com.pluxity.siteguard.constructionprogress.repository

import com.pluxity.siteguard.constructionprogress.entity.WorkType
import org.springframework.data.jpa.repository.JpaRepository

interface WorkTypeRepository : JpaRepository<WorkType, Long> {
    fun existsByName(name: String): Boolean
}
