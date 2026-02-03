package com.ticketing.order.service

import com.ticketing.order.domain.Concert
import com.ticketing.order.domain.ConcertRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class ConcertServiceTest @Autowired constructor(
    private val optimisticLockConcertFacade: OptimisticLockConcertFacade,
    private val concertRepository: ConcertRepository
) {

    private var concert: Concert? = null

    @BeforeEach
    fun setUp() {
        concert = concertRepository.save(Concert(availableSeats = 100, eventDateTime = LocalDateTime.now()))
    }

    @AfterEach
    fun tearDown() {
        concertRepository.deleteAll()
    }

    @Test
    fun `100명의 사용자가 동시에 좌석을 예매하면 재고가 0이 되어야 한다`() {
        // given
        val numberOfThreads = 100
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        // when
        for (i in 1..numberOfThreads) {
            executorService.submit {
                try {
                    optimisticLockConcertFacade.decreaseSeats(concert!!.id, 1)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        // then
        val result = concertRepository.findById(concert!!.id).get()
        assertThat(result.availableSeats).isEqualTo(0)
    }
}
