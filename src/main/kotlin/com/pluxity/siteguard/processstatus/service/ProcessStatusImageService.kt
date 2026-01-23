package com.pluxity.siteguard.processstatus.service

import com.pluxity.siteguard.file.service.FileService
import com.pluxity.siteguard.processstatus.dto.ProcessStatusImageRequest
import com.pluxity.siteguard.processstatus.dto.ProcessStatusImageResponse
import com.pluxity.siteguard.processstatus.dto.toEntity
import com.pluxity.siteguard.processstatus.dto.toResponse
import com.pluxity.siteguard.processstatus.entity.ProcessStatusImage
import com.pluxity.siteguard.processstatus.repository.ProcessStatusImageRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProcessStatusImageService(
    private val repository: ProcessStatusImageRepository,
    private val fileService: FileService,
) {
    companion object {
        private const val PROCESS_STATUS_IMAGE = "process-status-image/"
    }

    @Transactional(readOnly = true)
    fun getImage(): ProcessStatusImageResponse {
        val image =
            repository.findByIdOrNull(ProcessStatusImage.SINGLETON_ID)
                ?: return ProcessStatusImageResponse()
        val fileResponse = fileService.getFileResponse(image.fileId)
        return image.toResponse(fileResponse)
    }

    @Transactional
    fun saveImage(request: ProcessStatusImageRequest) {
        repository
            .findByIdOrNull(ProcessStatusImage.SINGLETON_ID)
            ?.apply { update(request.fileId) }
            ?: repository.save(request.toEntity())

        fileService.finalizeUpload(request.fileId, "$PROCESS_STATUS_IMAGE${ProcessStatusImage.SINGLETON_ID}")
    }
}
