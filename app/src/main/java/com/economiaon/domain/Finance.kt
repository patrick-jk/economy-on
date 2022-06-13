package com.economiaon.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

data class Finance(
    val id: Long, var name: String, var type: FinanceType,
    @SerializedName("value") var financePrice: BigDecimal, var initialDate: LocalDate,
    var finalDate: LocalDate, @SerializedName("user_id") val user: User
)
