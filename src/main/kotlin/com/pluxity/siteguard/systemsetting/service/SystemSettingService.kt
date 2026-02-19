package com.pluxity.siteguard.systemsetting.service

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
) {
    fun find(): SystemSettingResponse =
        systemSettingRepository.findByIdOrNull(SystemSetting.SINGLETON_ID)?.toResponse()
            ?: SystemSettingResponse()

    @Transactional
    fun update(request: SystemSettingRequest) {
        systemSettingRepository
            .findByIdOrNull(SystemSetting.SINGLETON_ID)
            ?.apply { update(request.rollingIntervalSeconds) }
            ?: systemSettingRepository.save(SystemSetting(rollingIntervalSeconds = request.rollingIntervalSeconds))
    }
}
