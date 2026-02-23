package com.pluxity.siteguard.systemsetting.service

import com.pluxity.siteguard.file.service.FileService
import com.pluxity.siteguard.systemsetting.dto.SystemSettingRequest
import com.pluxity.siteguard.systemsetting.dto.SystemSettingResponse
import com.pluxity.siteguard.systemsetting.dto.toResponse
import com.pluxity.siteguard.systemsetting.entity.SystemSetting
import com.pluxity.siteguard.systemsetting.repository.SystemSettingRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SystemSettingService(
    private val systemSettingRepository: SystemSettingRepository,
    private val fileService: FileService,
) {
    companion object {
        private const val BIM_THUMBNAIL_PATH = "system-settings/bim-thumbnail/"
        private const val AERIAL_VIEW_PATH = "system-settings/aerial-view/"
    }

    fun find(): SystemSettingResponse {
        val setting =
            systemSettingRepository.findByIdOrNull(SystemSetting.SINGLETON_ID)
                ?: return SystemSettingResponse()
        val bimThumbnailFile = fileService.getFileResponse(setting.bimThumbnailFileId)
        val aerialViewFile = fileService.getFileResponse(setting.aerialViewFileId)
        return setting.toResponse(bimThumbnailFile, aerialViewFile)
    }

    @Transactional
    fun update(request: SystemSettingRequest) {
        val setting =
            systemSettingRepository
                .findByIdOrNull(SystemSetting.SINGLETON_ID)
                ?.apply {
                    update(
                        rollingIntervalSeconds = request.rollingIntervalSeconds,
                        bimThumbnailFileId = request.bimThumbnailFileId,
                        aerialViewFileId = request.aerialViewFileId,
                    )
                }
                ?: systemSettingRepository.save(
                    SystemSetting(
                        rollingIntervalSeconds = request.rollingIntervalSeconds,
                        bimThumbnailFileId = request.bimThumbnailFileId,
                        aerialViewFileId = request.aerialViewFileId,
                    ),
                )

        request.bimThumbnailFileId?.let {
            fileService.finalizeUpload(it, "$BIM_THUMBNAIL_PATH${setting.id}/")
        }
        request.aerialViewFileId?.let {
            fileService.finalizeUpload(it, "$AERIAL_VIEW_PATH${setting.id}/")
        }
    }
}
