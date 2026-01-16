package com.pluxity.siteguard.constructionprogress.service

import com.pluxity.siteguard.constructionprogress.dto.WorkTypeRequest
import com.pluxity.siteguard.constructionprogress.dto.WorkTypeResponse
import com.pluxity.siteguard.constructionprogress.dto.toEntity
import com.pluxity.siteguard.constructionprogress.dto.toResponse
import com.pluxity.siteguard.constructionprogress.entity.WorkType
import com.pluxity.siteguard.constructionprogress.repository.WorkTypeRepository
import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkTypeService(
    private val repository: WorkTypeRepository,
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
        repository.deleteById(getById(id).requiredId)
    }

    fun getById(id: Long): WorkType =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_WORK_TYPE, id)

    private fun validateNameUnique(name: String) {
        if (repository.existsByName(name)) {
            throw CustomException(ErrorCode.DUPLICATE_WORK_TYPE, name)
        }
    }
}
