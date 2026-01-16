package com.pluxity.siteguard.targetmanagement.service

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.targetmanagement.dto.dummyTargetManagementBulkRequest
import com.pluxity.siteguard.targetmanagement.dto.dummyTargetManagementRequest
import com.pluxity.siteguard.targetmanagement.dto.dummyTargetManagementSearch
import com.pluxity.siteguard.targetmanagement.entity.TargetManagement
import com.pluxity.siteguard.targetmanagement.entity.dummyConstructionSection
import com.pluxity.siteguard.targetmanagement.entity.dummyTargetManagement
import com.pluxity.siteguard.targetmanagement.repository.TargetManagementRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class TargetManagementServiceTest :
    BehaviorSpec({

        val repository: TargetManagementRepository = mockk()
        val constructionSectionService: ConstructionSectionService = mockk()
        val service = TargetManagementService(repository, constructionSectionService)

        val section1 = dummyConstructionSection(id = 1L, name = "절토")
        val section2 = dummyConstructionSection(id = 2L, name = "도로공")
        val section3 = dummyConstructionSection(id = 3L, name = "교량공")

        Given("목표관리 조회") {

            When("전체 목록을 조회하면") {
                val entities =
                    listOf(
                        dummyTargetManagement(id = 1L, constructionSection = section1),
                        dummyTargetManagement(id = 2L, constructionSection = section2),
                        dummyTargetManagement(id = 3L, constructionSection = section3),
                    )
                val page =
                    PageImpl(
                        entities,
                        PageRequest.of(0, 10),
                        entities.size.toLong(),
                    )

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<TargetManagement>>>(),
                    )
                } returns page

                val result =
                    service.findAll(dummyTargetManagementSearch())

                Then("페이징된 결과가 반환된다") {
                    result.content.size shouldBe 3
                    result.pageNumber shouldBe 1
                    result.pageSize shouldBe 10
                    result.totalElements shouldBe 3
                }
            }

            When("빈 목록을 조회하면") {
                val page =
                    PageImpl(
                        emptyList<TargetManagement>(),
                        PageRequest.of(0, 10),
                        0,
                    )

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<TargetManagement>>>(),
                    )
                } returns page

                val result =
                    service.findAll(dummyTargetManagementSearch())

                Then("빈 결과가 반환된다") {
                    result.content.size shouldBe 0
                    result.totalElements shouldBe 0
                }
            }

            When("페이지 번호를 지정하여 조회하면") {
                val entities =
                    (11L..15L).map {
                        dummyTargetManagement(id = it, constructionSection = section1)
                    }
                val page =
                    PageImpl(
                        entities,
                        PageRequest.of(1, 10),
                        15,
                    )

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<TargetManagement>>>(),
                    )
                } returns page

                val result =
                    service.findAll(dummyTargetManagementSearch(page = 2))

                Then("해당 페이지의 결과가 반환된다") {
                    result.content.size shouldBe 5
                    result.pageNumber shouldBe 2
                    result.totalElements shouldBe 15
                    result.first shouldBe false
                }
            }
        }

        Given("목표관리 일괄 저장/수정/삭제") {

            When("id가 없는 데이터를 저장하면") {
                val request =
                    dummyTargetManagementBulkRequest(
                        upserts = listOf(dummyTargetManagementRequest(constructionSectionId = 1L)),
                    )

                every { constructionSectionService.getById(1L) } returns section1
                every { repository.save(any()) } returns dummyTargetManagement(constructionSection = section1)

                service.saveOrUpdateAll(request)

                Then("repository.save가 호출된다") {
                    verify(exactly = 1) { repository.save(any()) }
                }
            }

            When("id가 있는 데이터를 수정하면") {
                val existingEntity =
                    dummyTargetManagement(
                        id = 1L,
                        constructionSection = section1,
                        progressRate = 50.0f,
                    )

                val request =
                    dummyTargetManagementBulkRequest(
                        upserts =
                            listOf(
                                dummyTargetManagementRequest(
                                    id = 1L,
                                    constructionSectionId = 2L,
                                    progressRate = 100.0f,
                                ),
                            ),
                    )

                every { constructionSectionService.getById(2L) } returns section2
                every { repository.findByIdOrNull(1L) } returns existingEntity

                service.saveOrUpdateAll(request)

                Then("엔티티가 업데이트된다") {
                    existingEntity.constructionSection shouldBe section2
                    existingEntity.progressRate shouldBe 100.0f
                }
            }

            When("존재하지 않는 id로 수정하면") {
                val request =
                    dummyTargetManagementBulkRequest(
                        upserts =
                            listOf(
                                dummyTargetManagementRequest(id = 999L, constructionSectionId = 1L),
                            ),
                    )

                every { constructionSectionService.getById(1L) } returns section1
                every { repository.findByIdOrNull(999L) } returns null

                Then("CustomException이 발생한다") {
                    shouldThrow<CustomException> {
                        service.saveOrUpdateAll(request)
                    }
                }
            }

            When("삭제할 id 목록이 있으면") {
                val request =
                    dummyTargetManagementBulkRequest(
                        deletedIds = listOf(1L, 2L, 3L),
                    )

                every { repository.deleteAllById(listOf(1L, 2L, 3L)) } just runs

                service.saveOrUpdateAll(request)

                Then("repository.deleteAllById가 호출된다") {
                    verify(exactly = 1) { repository.deleteAllById(listOf(1L, 2L, 3L)) }
                }
            }

            When("저장, 수정, 삭제가 동시에 요청되면") {
                val existingEntity =
                    dummyTargetManagement(
                        id = 2L,
                        constructionSection = section2,
                        progressRate = 30.0f,
                    )

                val request =
                    dummyTargetManagementBulkRequest(
                        upserts =
                            listOf(
                                dummyTargetManagementRequest(constructionSectionId = 3L),
                                dummyTargetManagementRequest(
                                    id = 2L,
                                    constructionSectionId = 3L,
                                    progressRate = 100.0f,
                                ),
                            ),
                        deletedIds = listOf(5L, 6L),
                    )

                every { repository.deleteAllById(listOf(5L, 6L)) } just runs
                every { constructionSectionService.getById(3L) } returns section3
                every { repository.save(any()) } returns existingEntity
                every { repository.findByIdOrNull(2L) } returns existingEntity

                service.saveOrUpdateAll(request)

                Then("삭제, 저장, 수정이 모두 수행된다") {
                    verify(exactly = 1) { repository.deleteAllById(listOf(5L, 6L)) }
                    verify(exactly = 1) { repository.save(any()) }
                    existingEntity.constructionSection shouldBe section3
                }
            }
        }
    })
