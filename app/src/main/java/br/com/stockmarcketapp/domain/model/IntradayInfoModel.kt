package br.com.stockmarcketapp.domain.model

import java.time.LocalDateTime

data class IntradayInfoModel(
    val date: LocalDateTime,
    val close: Double
)
