package com.pluxity.siteguard.weather.repository

import com.pluxity.siteguard.weather.entity.Weather
import org.springframework.data.jpa.repository.JpaRepository

interface WeatherRepository : JpaRepository<Weather, Long> {
    fun findTopByOrderByIdDesc(): Weather?
}
