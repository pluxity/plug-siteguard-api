package com.pluxity.siteguard.weather

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/weather")
class WeatherController {
    @PostMapping(
        "/webhook",
        consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun receive(
        @RequestParam(name = "type") type: String,
        @RequestParam(name = "data") data: String,
    ): WebhookResponse {
        log.info { "Webhook Received $type $data" }

        return WebhookResponse(
            status = 0,
            msg = "성공",
        )
    }

    data class WebhookResponse(
        val status: Int,
        val msg: String,
    )
}
