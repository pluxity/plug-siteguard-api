package com.pluxity.siteguard.processstatus.service

import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.processstatus.dto.WorkTypeRequest
import com.pluxity.siteguard.processstatus.dto.WorkTypeResponse
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
    fun create(request: WorkTypeRequest): Long = repository.save(WorkType(name = request.name)).requiredId

    @Transactional
    fun delete(id: Long) {
        val workType = getById(id)
        if (processStatusRepository.existsByWorkType(workType)) {
            throw CustomException(ErrorCode.WORK_TYPE_HAS_PROCESS_STATUS)
        }
        repository.deleteById(workType.requiredId)
    }

    fun getById(id: Long) =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_WORK_TYPE, id)
}
