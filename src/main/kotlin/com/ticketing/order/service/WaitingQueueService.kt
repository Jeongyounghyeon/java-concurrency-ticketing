package com.ticketing.order.service

import com.ticketing.order.infra.WaitingQueueRepository
import org.springframework.stereotype.Service

@Service
class WaitingQueueService(
    private val waitingQueueRepository: WaitingQueueRepository
) {
    private val QUEUE_KEY = "waiting_queue"
    private val ACTIVE_TOKEN_KEY = "active_tokens"

    fun register(userId: String): Long {
        waitingQueueRepository.add(QUEUE_KEY, userId)
        return waitingQueueRepository.rank(QUEUE_KEY, userId) ?: -1
    }

    fun allowUsers(count: Long) {
        val allowedUsers = waitingQueueRepository.popMin(QUEUE_KEY, count)
        allowedUsers.forEach { userId ->
            waitingQueueRepository.addActiveToken(ACTIVE_TOKEN_KEY, userId)
        }
    }

    fun isAllowed(userId: String): Boolean {
        return waitingQueueRepository.isActive(ACTIVE_TOKEN_KEY, userId)
    }

    fun getRank(userId: String): Long {
        return waitingQueueRepository.rank(QUEUE_KEY, userId) ?: -1
    }
}
