package com.pluxity.siteguard.targetmanagement.repository

import com.pluxity.siteguard.targetmanagement.entity.ConstructionSection
import org.springframework.data.jpa.repository.JpaRepository

interface ConstructionSectionRepository : JpaRepository<ConstructionSection, Long> {
    fun existsByName(name: String): Boolean
}
