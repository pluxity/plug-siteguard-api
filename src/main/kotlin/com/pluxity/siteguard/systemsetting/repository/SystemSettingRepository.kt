package com.pluxity.siteguard.systemsetting.repository

import com.pluxity.siteguard.systemsetting.entity.SystemSetting
import org.springframework.data.jpa.repository.JpaRepository

interface SystemSettingRepository : JpaRepository<SystemSetting, Long>
