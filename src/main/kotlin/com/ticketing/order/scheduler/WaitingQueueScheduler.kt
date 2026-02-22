package com.ticketing.order.scheduler

import com.ticketing.order.service.WaitingQueueService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class WaitingQueueScheduler(
    private val waitingQueueService: WaitingQueueService
) {

    @Scheduled(fixedRate = 100) // 0.1초마다 실행
    fun allowUsers() {
        waitingQueueService.allowUsers(50) // 한 번에 50명씩 입장 (초당 500명)
    }
}
