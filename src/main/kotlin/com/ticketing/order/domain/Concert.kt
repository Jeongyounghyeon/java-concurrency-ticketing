package com.ticketing.order.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "concerts")
class Concert(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var availableSeats: Int,

    @Column(nullable = false)
    val eventDateTime: LocalDateTime,

    @Version
    val version: Long = 0
)
