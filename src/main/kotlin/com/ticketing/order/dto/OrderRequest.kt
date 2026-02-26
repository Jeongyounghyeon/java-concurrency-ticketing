package com.ticketing.order.dto

data class OrderRequest(
    val concertId: Long,
    val quantity: Int,
    val userId: String
)
