package com.pluxity.siteguard.constructionprogress.entity

import com.pluxity.siteguard.global.entity.IdentityIdEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "construction_progress")
class ConstructionProgress(
    @Column(name = "work_date", nullable = false)
    var workDate: LocalDate,
    @Enumerated(EnumType.STRING)
    @Column(name = "phase_name")
    var phaseName: PhaseName,
    @Column(name = "planned_rate")
    var plannedRate: Int,
    @Column(name = "actual_rate")
    var actualRate: Int,
) : IdentityIdEntity() {
    fun update(
        workDate: LocalDate,
        phaseName: PhaseName,
        plannedRate: Int,
        actualRate: Int,
    ) {
        this.workDate = workDate
        this.phaseName = phaseName
        this.plannedRate = plannedRate
        this.actualRate = actualRate
    }
}
