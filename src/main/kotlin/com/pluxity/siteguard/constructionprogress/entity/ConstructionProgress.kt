package com.pluxity.siteguard.constructionprogress.entity

import com.pluxity.siteguard.global.entity.IdentityIdEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "construction_progress")
class ConstructionProgress(
    @Column(name = "work_date", nullable = false)
    var workDate: LocalDate,
    @Column(name = "phase_name")
    var phaseName: String,
    @Column(name = "planned_rate")
    var plannedRate: Float,
    @Column(name = "actual_rate")
    var actualRate: Float,
    @Column(name = "planned_progress_rate")
    var plannedProgressRate: Float,
    @Column(name = "actual_progress_rate")
    var actualProgressRate: Float,
) : IdentityIdEntity() {
    fun update(
        workDate: LocalDate,
        phaseName: String,
        plannedRate: Float,
        actualRate: Float,
        plannedProgressRate: Float,
        actualProgressRate: Float,
    ) {
        this.workDate = workDate
        this.phaseName = phaseName
        this.plannedRate = plannedRate
        this.actualRate = actualRate
        this.plannedProgressRate = plannedProgressRate
        this.actualProgressRate = actualProgressRate
    }
}
