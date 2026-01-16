package com.pluxity.siteguard.constructionprogress.entity

import com.pluxity.siteguard.global.entity.IdentityIdEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "construction_progress")
class ConstructionProgress(
    @Column(name = "work_date", nullable = false)
    var workDate: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_type_id", nullable = false)
    var workType: WorkType,
    @Column(name = "planned_rate")
    var plannedRate: Int,
    @Column(name = "actual_rate")
    var actualRate: Int,
) : IdentityIdEntity() {
    fun update(
        workDate: LocalDate,
        workType: WorkType,
        plannedRate: Int,
        actualRate: Int,
    ) {
        this.workDate = workDate
        this.workType = workType
        this.plannedRate = plannedRate
        this.actualRate = actualRate
    }
}
