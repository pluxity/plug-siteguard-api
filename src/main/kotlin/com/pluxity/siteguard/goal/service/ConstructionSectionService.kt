package com.pluxity.siteguard.goal.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.goal.dto.ConstructionSectionRequest
import com.pluxity.siteguard.goal.dto.ConstructionSectionResponse
import com.pluxity.siteguard.goal.dto.toEntity
import com.pluxity.siteguard.goal.dto.toResponse
import com.pluxity.siteguard.goal.entity.ConstructionSection
import com.pluxity.siteguard.goal.repository.ConstructionSectionRepository
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
