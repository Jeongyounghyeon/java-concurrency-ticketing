package com.ticketing.order.infra

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class WaitingQueueRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun add(queueKey: String, userId: String): Boolean {
        val now = System.currentTimeMillis()
        return redisTemplate.opsForZSet().add(queueKey, userId, now.toDouble()) ?: false
    }

    fun rank(queueKey: String, userId: String): Long? {
        return redisTemplate.opsForZSet().rank(queueKey, userId)
    }

    fun popMin(queueKey: String, count: Long): Set<String> {
        val range = redisTemplate.opsForZSet().range(queueKey, 0, count - 1)
        if (!range.isNullOrEmpty()) {
            redisTemplate.opsForZSet().remove(queueKey, *range.toTypedArray())
        }
        return range ?: emptySet()
    }
    
    // 활성 토큰 관리 (Set 구조 사용)
    fun addActiveToken(tokenKey: String, userId: String) {
        redisTemplate.opsForSet().add(tokenKey, userId)
        redisTemplate.expire(tokenKey, 5, TimeUnit.MINUTES) // 5분 후 만료
    }

    fun isActive(tokenKey: String, userId: String): Boolean {
        return redisTemplate.opsForSet().isMember(tokenKey, userId) ?: false
    }
}
