package com.pluxity.siteguard.weather.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.pluxity.siteguard.weather.entity.Weather

data class WeatherDataDto(
    @field:JsonProperty("검침시간")
    val measuredAt: String,
    @field:JsonProperty("온도")
    val temperature: Double,
    @field:JsonProperty("습도")
    val humidity: Double,
    @field:JsonProperty("풍속")
    val windSpeed: Double,
    @field:JsonProperty("풍향")
    val windDirection: String,
    @field:JsonProperty("강우량")
    val rainfall: Int,
    @field:JsonProperty("미세먼지")
    val pm10: Int,
    @field:JsonProperty("초미세먼지")
    val pm25: Int,
    @field:JsonProperty("미세먼지상태")
    val pm10Status: String,
    @field:JsonProperty("초미세먼지상태")
    val pm25Status: String,
    @field:JsonProperty("소음")
    val noise: Double,
)

fun WeatherDataDto.toEntity(): Weather =
    Weather(
        measuredAt = this.measuredAt,
        temperature = this.temperature,
        humidity = this.humidity,
        windSpeed = this.windSpeed,
        windDirection = this.windDirection,
        rainfall = this.rainfall,
        pm10 = this.pm10,
        pm25 = this.pm25,
        pm10Status = this.pm10Status,
        pm25Status = this.pm25Status,
        noise = this.noise,
    )
