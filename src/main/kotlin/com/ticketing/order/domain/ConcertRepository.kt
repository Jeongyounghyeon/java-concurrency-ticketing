package com.ticketing.order.domain

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface ConcertRepository : JpaRepository<Concert, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Concert c where c.id = :id")
    fun findByIdWithPessimisticLock(id: Long): Optional<Concert>
}
