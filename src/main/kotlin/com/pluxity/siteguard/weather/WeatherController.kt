package com.pluxity.siteguard.weather

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@Hidden
@RestController
@RequestMapping("/weather")
class WeatherController(
    private val objectMapper: ObjectMapper,
) {
    @PostMapping("/webhook")
    fun receive(
        @RequestBody request: IncomingRequest,
    ): WebhookResponse {
        log.info { "Webhook Received ${request.type} ${request.data}" }

        if (request.type == "WEATHER") {
            val weather: WeatherData =
                objectMapper.readValue(request.data, WeatherData::class.java)
            log.info { weather }
        }

        return WebhookResponse(
            status = 0,
            msg = "성공",
        )
    }

    data class WeatherData(
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

    data class WebhookResponse(
        val status: Int,
        val msg: String,
    )

    data class IncomingRequest(
        val type: String,
        val data: String,
    )
}
