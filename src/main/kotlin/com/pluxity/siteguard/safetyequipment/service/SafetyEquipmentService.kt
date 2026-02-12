package com.pluxity.siteguard.safetyequipment.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.safetyequipment.dto.SafetyEquipmentRequest
import com.pluxity.siteguard.safetyequipment.dto.SafetyEquipmentResponse
import com.pluxity.siteguard.safetyequipment.dto.toResponse
import com.pluxity.siteguard.safetyequipment.entity.SafetyEquipment
import com.pluxity.siteguard.safetyequipment.repository.SafetyEquipmentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SafetyEquipmentService(
    private val safetyEquipmentRepository: SafetyEquipmentRepository,
) {
    @Transactional
    fun create(request: SafetyEquipmentRequest): Long {
        val safetyEquipment =
            SafetyEquipment(
                name = request.name,
                quantity = request.quantity,
            )

        val saved = safetyEquipmentRepository.save(safetyEquipment)
        return saved.requiredId
    }

    fun findAll(): List<SafetyEquipmentResponse> = safetyEquipmentRepository.findAll().map { it.toResponse() }

    fun findById(id: Long): SafetyEquipmentResponse = getById(id).toResponse()

    @Transactional
    fun update(
        id: Long,
        request: SafetyEquipmentRequest,
    ) {
        val safetyEquipment = getById(id)
        safetyEquipment.update(
            name = request.name,
            quantity = request.quantity,
        )
    }

    @Transactional
    fun delete(id: Long) {
        val safetyEquipment = getById(id)
        safetyEquipmentRepository.delete(safetyEquipment)
    }

    private fun getById(id: Long): SafetyEquipment =
        safetyEquipmentRepository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_SAFETY_EQUIPMENT, id)
}
