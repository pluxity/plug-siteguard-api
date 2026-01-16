package com.pluxity.siteguard.goal.repository

import com.pluxity.siteguard.goal.entity.ConstructionSection
import org.springframework.data.jpa.repository.JpaRepository

interface ConstructionSectionRepository : JpaRepository<ConstructionSection, Long> {
    fun existsByName(name: String): Boolean
}
