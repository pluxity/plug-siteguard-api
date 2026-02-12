package com.pluxity.siteguard.safetyequipment.repository

import com.pluxity.siteguard.safetyequipment.entity.SafetyEquipment
import org.springframework.data.jpa.repository.JpaRepository

interface SafetyEquipmentRepository : JpaRepository<SafetyEquipment, Long>
