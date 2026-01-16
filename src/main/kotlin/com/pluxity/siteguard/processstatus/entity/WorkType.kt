package com.pluxity.siteguard.processstatus.entity

import com.pluxity.siteguard.global.entity.IdentityIdEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "work_type")
class WorkType(
    @Column(name = "name", nullable = false, unique = true)
    val name: String,
) : IdentityIdEntity()
