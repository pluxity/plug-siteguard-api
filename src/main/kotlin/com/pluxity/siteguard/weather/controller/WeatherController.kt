package com.pluxity.siteguard.weather.controller

import com.pluxity.siteguard.global.response.DataResponseBody
import com.pluxity.siteguard.weather.dto.IncomingRequest
import com.pluxity.siteguard.weather.dto.WeatherResponse
import com.pluxity.siteguard.weather.dto.WebhookResponse
import com.pluxity.siteguard.weather.service.WeatherService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/weather")
@Tag(name = "Weather Controller", description = "날씨 정보 API")
class WeatherController(
    private val weatherService: WeatherService,
) {
    @Operation(summary = "최신 날씨 정보 조회", description = "가장 최근에 수신된 날씨 정보를 조회합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "조회 성공"),
        ],
    )
    @GetMapping("")
    fun getLatest(): ResponseEntity<DataResponseBody<WeatherResponse>> = ResponseEntity.ok(DataResponseBody(weatherService.findLatest()))

    @Hidden
    @PostMapping("/webhook")
    fun receive(
        @RequestBody request: IncomingRequest,
    ): WebhookResponse {
        log.info { "Webhook Received ${request.type} ${request.data}" }

        if (request.type == "WEATHER") {
            weatherService.saveFromJson(request.data)
        }

        return WebhookResponse(
            status = 0,
            msg = "성공",
        )
    }
}
