package com.pluxity.siteguard.processstatus.repository

import com.pluxity.siteguard.processstatus.entity.ProcessStatusImage
import org.springframework.data.jpa.repository.JpaRepository

interface ProcessStatusImageRepository : JpaRepository<ProcessStatusImage, Long>
