package com.pluxity.siteguard.targetmanagement.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.pluxity.siteguard.targetmanagement.entity.TargetManagement
import org.springframework.data.jpa.repository.JpaRepository

interface TargetManagementRepository :
    JpaRepository<TargetManagement, Long>,
    KotlinJdslJpqlExecutor
