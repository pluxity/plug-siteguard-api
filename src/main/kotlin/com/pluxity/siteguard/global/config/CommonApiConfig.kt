package com.pluxity.siteguard.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonApiConfig {
    @Bean
    @ConditionalOnMissingBean(OpenAPI::class)
    fun commonOpenAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Yongin Platform API")
                    .description("Yongin Platform API Documentation")
                    .version("1.0.0")
                    .contact(Contact().name("Pluxity").email("support@pluxity.com"))
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("http://www.apache.org/licenses/LICENSE-2.0.html"),
                    ),
            )

    @Bean
    fun commonApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("1. 전체")
            .pathsToMatch("/**")
            .build()

    @Bean
    fun authApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("2. 인증")
            .pathsToMatch("/auth/**")
            .pathsToExclude("/users/**", "/admin/**", "/other/**") // 제외 경로 추가
            .build()

    @Bean
    fun fileApiByPath(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("3. 파일관리 API")
            .pathsToMatch("/files/**")
            .build()

    @Bean
    fun userApiByPath(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("4. 사용자 API")
            .pathsToMatch("/users/**", "/admin/users/**", "/roles/**", "/permissions/**")
            .build()

    @Bean
    fun processStatusApiByPath(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("5. 공정현황 관리 API")
            .pathsToMatch("/process-statuses/**")
            .build()

    @Bean
    fun goalApiByPath(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("6. 목표 관리 API")
            .pathsToMatch("/goals/**")
            .build()

    @Bean
    fun targetManagementApiByPath(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("7. 주요관리사항 관리 API")
            .pathsToMatch("/key-management/**")
            .build()
}
