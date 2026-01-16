package com.pluxity.siteguard.goal.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.pluxity.siteguard.goal.entity.Goal
import org.springframework.data.jpa.repository.JpaRepository

interface GoalRepository :
    JpaRepository<Goal, Long>,
    KotlinJdslJpqlExecutor
