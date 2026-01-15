package com.pluxity.siteguard.constructionprogress.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.pluxity.siteguard.constructionprogress.entity.ConstructionProgress
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface ConstructionProgressRepository :
    JpaRepository<ConstructionProgress, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllBy(sort: Sort): List<ConstructionProgress>
}
