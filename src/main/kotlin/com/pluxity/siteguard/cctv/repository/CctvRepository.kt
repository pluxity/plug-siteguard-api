package com.pluxity.siteguard.cctv.repository

import com.pluxity.siteguard.cctv.entity.Cctv
import org.springframework.data.jpa.repository.JpaRepository

interface CctvRepository : JpaRepository<Cctv, Long>
