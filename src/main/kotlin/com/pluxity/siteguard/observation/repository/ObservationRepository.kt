package com.pluxity.siteguard.observation.repository

import com.pluxity.siteguard.observation.entity.Observation
import org.springframework.data.jpa.repository.JpaRepository

interface ObservationRepository : JpaRepository<Observation, Long>
