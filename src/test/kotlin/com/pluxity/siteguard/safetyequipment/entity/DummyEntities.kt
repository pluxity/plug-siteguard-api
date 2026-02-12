package com.pluxity.siteguard.safetyequipment.entity

import com.pluxity.siteguard.base.entity.withAudit
import com.pluxity.siteguard.base.entity.withId

fun dummySafetyEquipment(
    id: Long? = 1L,
    name: String = "안전모",
    quantity: Int = 100,
): SafetyEquipment =
    SafetyEquipment(
        name = name,
        quantity = quantity,
    ).withId(id).withAudit()
