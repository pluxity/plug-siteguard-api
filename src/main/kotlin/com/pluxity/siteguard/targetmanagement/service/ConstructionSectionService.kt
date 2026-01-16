package com.pluxity.siteguard.targetmanagement.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.targetmanagement.dto.ConstructionSectionRequest
import com.pluxity.siteguard.targetmanagement.dto.ConstructionSectionResponse
import com.pluxity.siteguard.targetmanagement.dto.toEntity
import com.pluxity.siteguard.targetmanagement.dto.toResponse
import com.pluxity.siteguard.targetmanagement.entity.ConstructionSection
import com.pluxity.siteguard.targetmanagement.repository.ConstructionSectionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConstructionSectionService(
    private val repository: ConstructionSectionRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<ConstructionSectionResponse> = repository.findAll().map { it.toResponse() }

    @Transactional
    fun create(request: ConstructionSectionRequest): Long {
        validateNameUnique(request.name)
        return repository.save(request.toEntity()).requiredId
    }

    @Transactional
    fun delete(id: Long) {
        repository.deleteById(getById(id).requiredId)
    }

    fun getById(id: Long): ConstructionSection =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_CONSTRUCTION_SECTION, id)

    private fun validateNameUnique(name: String) {
        if (repository.existsByName(name)) {
            throw CustomException(ErrorCode.DUPLICATE_CONSTRUCTION_SECTION, name)
        }
    }
}
