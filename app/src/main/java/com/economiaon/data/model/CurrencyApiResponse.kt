package com.economiaon.data.model

import com.google.gson.annotations.SerializedName

data class CurrencyApiResponse(
    @SerializedName("new_amount") val newAmount: Double,
    @SerializedName("old_amount") val oldAmount: Double,
)
