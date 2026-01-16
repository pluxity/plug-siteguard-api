package com.pluxity.siteguard.constructionprogress.service

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQueryable
import com.linecorp.kotlinjdsl.querymodel.jpql.select.SelectQuery
import com.pluxity.siteguard.constructionprogress.dto.dummyProcessStatusBulkRequest
import com.pluxity.siteguard.constructionprogress.dto.dummyProcessStatusRequest
import com.pluxity.siteguard.constructionprogress.dto.dummyProcessStatusSearch
import com.pluxity.siteguard.constructionprogress.entity.ProcessStatus
import com.pluxity.siteguard.constructionprogress.entity.dummyProcessStatus
import com.pluxity.siteguard.constructionprogress.entity.dummyWorkType
import com.pluxity.siteguard.constructionprogress.repository.ProcessStatusRepository
import com.pluxity.siteguard.global.exception.CustomException
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
import java.time.LocalDate

class ProcessStatusServiceTest :
    BehaviorSpec({

        val repository: ProcessStatusRepository = mockk()
        val workTypeService: WorkTypeService = mockk()
        val service = ProcessStatusService(repository, workTypeService)

        val earthwork = dummyWorkType(id = 1L, name = "토공")
        val road = dummyWorkType(id = 2L, name = "도로공")
        val nonOpenCut = dummyWorkType(id = 3L, name = "비개착")
        val bridgeRetainingWall = dummyWorkType(id = 4L, name = "교량/옹벽")

        Given("공정현황 조회") {

            When("전체 목록을 조회하면") {
                val entities =
                    listOf(
                        dummyProcessStatus(id = 1L, workType = earthwork, workDate = LocalDate.of(2026, 1, 15)),
                        dummyProcessStatus(id = 2L, workType = road, workDate = LocalDate.of(2026, 1, 14)),
                        dummyProcessStatus(id = 3L, workType = nonOpenCut, workDate = LocalDate.of(2026, 1, 13)),
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
                        any<Jpql.() -> JpqlQueryable<SelectQuery<ProcessStatus>>>(),
                    )
                } returns page

                val result = service.findAll(dummyProcessStatusSearch())

                Then("페이징된 결과가 반환된다") {
                    result.content.size shouldBe 3
                    result.pageNumber shouldBe 1
                    result.pageSize shouldBe 10
                    result.totalElements shouldBe 3
                }
            }

            When("빈 목록을 조회하면") {
                val pageable = PageRequest.of(0, 10)
                val page = PageImpl(emptyList<ProcessStatus>(), pageable, 0)

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<
                            Jpql.() ->
                            JpqlQueryable<SelectQuery<ProcessStatus>>,
                        >(),
                    )
                } returns page

                val result = service.findAll(dummyProcessStatusSearch())

                Then("빈 결과가 반환된다") {
                    result.content.size shouldBe 0
                    result.totalElements shouldBe 0
                }
            }

            When("페이지 번호를 지정하여 조회하면") {
                val entities = (11L..15L).map { dummyProcessStatus(id = it, workType = bridgeRetainingWall) }
                val page = PageImpl(entities, PageRequest.of(1, 10), 15)

                every {
                    repository.findPage(
                        any<Pageable>(),
                        any<Jpql.() -> JpqlQueryable<SelectQuery<ProcessStatus>>>(),
                    )
                } returns page

                val result = service.findAll(dummyProcessStatusSearch(page = 2))

                Then("해당 페이지의 결과가 반환된다") {
                    result.content.size shouldBe 5
                    result.pageNumber shouldBe 2
                    result.totalElements shouldBe 15
                    result.first shouldBe false
                }
            }
        }

        Given("공정현황 일괄 저장/수정/삭제") {

            When("id가 없는 데이터를 저장하면") {
                val request =
                    dummyProcessStatusBulkRequest(
                        upserts = listOf(dummyProcessStatusRequest(workTypeId = 1L)),
                    )

                every { workTypeService.getById(1L) } returns earthwork
                every { repository.save(any()) } returns dummyProcessStatus()

                service.saveOrUpdateAll(request)

                Then("repository.save가 호출된다") {
                    verify(exactly = 1) { repository.save(any()) }
                }
            }

            When("id가 있는 데이터를 수정하면") {
                val existingEntity =
                    dummyProcessStatus(
                        id = 1L,
                        workType = earthwork,
                        workDate = LocalDate.of(2026, 1, 10),
                        plannedRate = 80,
                        actualRate = 75,
                    )

                val request =
                    dummyProcessStatusBulkRequest(
                        upserts =
                            listOf(
                                dummyProcessStatusRequest(
                                    id = 1L,
                                    workTypeId = 2L,
                                ),
                            ),
                    )

                every { workTypeService.getById(2L) } returns road
                every { repository.findByIdOrNull(1L) } returns existingEntity

                service.saveOrUpdateAll(request)

                Then("엔티티가 업데이트된다") {
                    existingEntity.workType shouldBe road
                    existingEntity.plannedRate shouldBe 100
                    existingEntity.actualRate shouldBe 100
                }
            }

            When("존재하지 않는 id로 수정하면") {
                val request =
                    dummyProcessStatusBulkRequest(
                        upserts =
                            listOf(
                                dummyProcessStatusRequest(id = 999L, workTypeId = 1L),
                            ),
                    )

                every { workTypeService.getById(1L) } returns earthwork
                every { repository.findByIdOrNull(999L) } returns null

                Then("CustomException이 발생한다") {
                    shouldThrow<CustomException> {
                        service.saveOrUpdateAll(request)
                    }
                }
            }

            When("삭제할 id 목록이 있으면") {
                val request =
                    dummyProcessStatusBulkRequest(
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
                    dummyProcessStatus(
                        id = 2L,
                        workType = road,
                        plannedRate = 50,
                        actualRate = 45,
                    )

                val request =
                    dummyProcessStatusBulkRequest(
                        upserts =
                            listOf(
                                dummyProcessStatusRequest(workTypeId = 1L),
                                dummyProcessStatusRequest(id = 2L, workTypeId = 3L),
                            ),
                        deletedIds = listOf(5L, 6L),
                    )

                every { repository.deleteAllById(listOf(5L, 6L)) } just runs
                every { workTypeService.getById(1L) } returns earthwork
                every { workTypeService.getById(3L) } returns nonOpenCut
                every { repository.save(any()) } returns existingEntity
                every { repository.findByIdOrNull(2L) } returns existingEntity

                service.saveOrUpdateAll(request)

                Then("삭제, 저장, 수정이 모두 수행된다") {
                    verify(exactly = 1) { repository.deleteAllById(listOf(5L, 6L)) }
                    verify(exactly = 1) { repository.save(any()) }
                    existingEntity.workType shouldBe nonOpenCut
                }
            }
        }
    })
