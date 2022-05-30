package com.economiaon.domain

import java.math.BigDecimal
import java.time.LocalDate
import kotlin.time.Duration

data class Finance(var name: String, val type: FinanceType, val duration: Duration,
                   val financePrice: BigDecimal, val initialDate: LocalDate,
                   val finalDate: LocalDate)
