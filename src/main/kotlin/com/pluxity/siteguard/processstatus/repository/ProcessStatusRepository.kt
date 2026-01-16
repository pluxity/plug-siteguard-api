package com.pluxity.siteguard.processstatus.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.pluxity.siteguard.processstatus.entity.ProcessStatus
import com.pluxity.siteguard.processstatus.entity.WorkType
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface ProcessStatusRepository :
    JpaRepository<ProcessStatus, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllBy(sort: Sort): List<ProcessStatus>

    fun existsByWorkDateAndWorkType(
        workDate: LocalDate,
        workType: WorkType,
    ): Boolean

    fun existsByWorkDateAndWorkTypeAndIdNot(
        workDate: LocalDate,
        workType: WorkType,
        id: Long,
    ): Boolean

    @Query("SELECT p FROM ProcessStatus p WHERE p.workDate = (SELECT MAX(p2.workDate) FROM ProcessStatus p2)")
    fun findAllByLatestWorkDate(): List<ProcessStatus>
}
