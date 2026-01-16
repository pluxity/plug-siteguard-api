package com.pluxity.siteguard.processstatus.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.pluxity.siteguard.processstatus.entity.ProcessStatus
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessStatusRepository :
    JpaRepository<ProcessStatus, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllBy(sort: Sort): List<ProcessStatus>
}
