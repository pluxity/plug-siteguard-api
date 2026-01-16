package com.pluxity.siteguard.goal.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.pluxity.siteguard.goal.entity.ConstructionSection
import com.pluxity.siteguard.goal.entity.Goal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface GoalRepository :
    JpaRepository<Goal, Long>,
    KotlinJdslJpqlExecutor {
    @Query("SELECT g FROM Goal g WHERE g.inputDate = (SELECT MAX(g2.inputDate) FROM Goal g2)")
    fun findAllByLatestInputDate(): List<Goal>

    fun existsByInputDateAndConstructionSection(
        inputDate: LocalDate,
        constructionSection: ConstructionSection,
    ): Boolean

    fun existsByInputDateAndConstructionSectionAndIdNot(
        inputDate: LocalDate,
        constructionSection: ConstructionSection,
        id: Long,
    ): Boolean
}
