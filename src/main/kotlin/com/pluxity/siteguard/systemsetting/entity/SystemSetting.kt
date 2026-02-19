package com.pluxity.siteguard.systemsetting.entity

import com.pluxity.siteguard.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "system_settings")
class SystemSetting(
    @Id
    @Column(name = "id", nullable = false)
    val id: Long = SINGLETON_ID,
    @Column(name = "rolling_interval_seconds", nullable = false)
    var rollingIntervalSeconds: Int,
) : BaseEntity() {
    fun update(rollingIntervalSeconds: Int) {
        this.rollingIntervalSeconds = rollingIntervalSeconds
    }

    companion object {
        const val SINGLETON_ID = 1L
    }
}
