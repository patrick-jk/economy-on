package com.economiaon.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Finance(
    val id: Long, var name: String, var type: FinanceType,
    @SerializedName("value") var financePrice: BigDecimal,
    @SerializedName("initial_date") var initialDate: String,
    @SerializedName("final_date") var finalDate: String,
    @SerializedName("user_id") val user: User) : Parcelable
