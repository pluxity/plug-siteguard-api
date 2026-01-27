package com.pluxity.siteguard.keymanagement.service

import com.pluxity.siteguard.file.extensions.getFileMapById
import com.pluxity.siteguard.file.service.FileService
import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.keymanagement.dto.KeyManagementGroupResponse
import com.pluxity.siteguard.keymanagement.dto.KeyManagementRequest
import com.pluxity.siteguard.keymanagement.dto.KeyManagementResponse
import com.pluxity.siteguard.keymanagement.dto.KeyManagementUpdateRequest
import com.pluxity.siteguard.keymanagement.dto.toEntity
import com.pluxity.siteguard.keymanagement.dto.toResponse
import com.pluxity.siteguard.keymanagement.entity.KeyManagement
import com.pluxity.siteguard.keymanagement.entity.KeyManagementType
import com.pluxity.siteguard.keymanagement.repository.KeyManagementRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class KeyManagementService(
    private val repository: KeyManagementRepository,
    private val fileService: FileService,
) {
    companion object {
        private const val KEY_MANAGEMENT: String = "key-management/"
    }

    @Transactional(readOnly = true)
    fun findAll(): List<KeyManagementGroupResponse> {
        val keyManagements = repository.findAll()

        val fileMap = fileService.getFileMapById(keyManagements) { it.fileId }

        return KeyManagementType.sortedEntries().map { type ->
            KeyManagementGroupResponse(
                type = type,
                typeDescription = type.description,
                items =
                    keyManagements
                        .filter { it.type == type }
                        .sortedBy { it.displayOrder }
                        .map { it.toResponse(it.fileId?.let { id -> fileMap[id] }) },
            )
        }
    }

    @Transactional(readOnly = true)
    fun findSelected(): List<KeyManagementResponse> {
        val keyManagements = repository.findBySelectedTrue()

        val fileMap = fileService.getFileMapById(keyManagements) { it.fileId }

        return keyManagements
            .sortedWith(compareBy({ it.type.order }, { it.displayOrder }))
            .map { it.toResponse(it.fileId?.let { id -> fileMap[id] }) }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): KeyManagementResponse {
        val keyManagement = getById(id)
        return keyManagement.toResponse(keyManagement.fileId?.let { fileService.getFileResponse(it) })
    }

    @Transactional
    fun create(request: KeyManagementRequest): Long {
        validateDisplayOrderUnique(request.type, request.displayOrder)
        val savedKeyManagement = repository.save(request.toEntity())
        request.fileId?.let {
            fileService.finalizeUpload(it, "${KEY_MANAGEMENT}${savedKeyManagement.requiredId}/")
        }
        return savedKeyManagement.requiredId
    }

    @Transactional
    fun update(
        id: Long,
        request: KeyManagementUpdateRequest,
    ) {
        val keyManagement = getById(id)

        keyManagement.update(
            type = request.type,
            title = request.title,
            methodFeature = request.methodFeature,
            methodContent = request.methodContent,
            methodDirection = request.methodDirection,
            fileId = request.fileId,
        )

        if (keyManagement.fileId != request.fileId) {
            request.fileId?.let {
                fileService.finalizeUpload(it, "${KEY_MANAGEMENT}${keyManagement.requiredId}/")
            }
        }
    }

    @Transactional
    fun delete(id: Long) {
        repository.deleteById(getById(id).requiredId)
    }

    @Transactional
    fun select(id: Long) = getById(id).select()

    @Transactional
    fun deselect(id: Long) = getById(id).deselect()

    private fun getById(id: Long): KeyManagement =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_KEY_MANAGEMENT, id)

    private fun validateDisplayOrderUnique(
        type: KeyManagementType,
        displayOrder: Int,
    ) {
        if (repository.existsByTypeAndDisplayOrder(type, displayOrder)) {
            throw CustomException(ErrorCode.DUPLICATE_KEY_MANAGEMENT_DISPLAY_ORDER, type.description, displayOrder)
        }
    }
}
