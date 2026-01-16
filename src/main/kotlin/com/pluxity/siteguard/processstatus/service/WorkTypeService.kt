package com.pluxity.siteguard.processstatus.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.processstatus.dto.WorkTypeRequest
import com.pluxity.siteguard.processstatus.dto.WorkTypeResponse
import com.pluxity.siteguard.processstatus.dto.toEntity
import com.pluxity.siteguard.processstatus.dto.toResponse
import com.pluxity.siteguard.processstatus.entity.WorkType
import com.pluxity.siteguard.processstatus.repository.ProcessStatusRepository
import com.pluxity.siteguard.processstatus.repository.WorkTypeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkTypeService(
    private val repository: WorkTypeRepository,
    private val processStatusRepository: ProcessStatusRepository,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<WorkTypeResponse> = repository.findAll().map { it.toResponse() }

    @Transactional
    fun create(request: WorkTypeRequest): Long {
        validateNameUnique(request.name)
        return repository.save(request.toEntity()).requiredId
    }

    @Transactional
    fun delete(id: Long) {
        val workType = getById(id)
        if (processStatusRepository.existsByWorkType(workType)) {
            throw CustomException(ErrorCode.WORK_TYPE_HAS_PROCESS_STATUS)
        }
        repository.deleteById(workType.requiredId)
    }

    fun getById(id: Long): WorkType =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_WORK_TYPE, id)

    fun getAllByIds(ids: List<Long>): Map<Long, WorkType> {
        val workTypes = repository.findAllById(ids)
        val foundIds = workTypes.map { it.requiredId }.toSet()
        val notFoundIds = ids.filter { it !in foundIds }
        if (notFoundIds.isNotEmpty()) {
            throw CustomException(ErrorCode.NOT_FOUND_WORK_TYPE, notFoundIds)
        }
        return workTypes.associateBy { it.requiredId }
    }

    private fun validateNameUnique(name: String) {
        if (repository.existsByName(name)) {
            throw CustomException(ErrorCode.DUPLICATE_WORK_TYPE, name)
        }
    }
}
