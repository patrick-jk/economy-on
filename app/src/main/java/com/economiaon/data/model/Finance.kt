package com.economiaon.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDate

@Parcelize
data class Finance(
    val id: Long = 0, var name: String, var type: FinanceType,
    @SerializedName("value") var financePrice: BigDecimal, var initialDate: LocalDate,
    var finalDate: LocalDate, @SerializedName("user_id") val user: User) : Parcelable
