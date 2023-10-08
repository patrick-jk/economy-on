package com.economiaon.domain.model

import android.os.Parcelable
import com.economiaon.data.local.entity.FinanceEntity
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Finance(
    var id: String = "",
    var name: String,
    var type: FinanceType,
    @get:PropertyName("value") @set:PropertyName("value") var financeCost: Double,
    @get:PropertyName("initial_date") @set:PropertyName("initial_date") var initialDate: String,
    @get:PropertyName("final_date") @set:PropertyName("final_date") var finalDate: String,
    @get:PropertyName("user_email") @set:PropertyName("user_email") var userEmail: String
) : Parcelable {
    constructor() : this(
        name = "",
        type = FinanceType.LEISURE,
        financeCost = 0.0,
        initialDate = "",
        finalDate = "",
        userEmail = ""
    )

    fun toFinanceEntity() = FinanceEntity(
        id = id,
        name = name,
        type = type,
        financeCost = financeCost,
        initialDate = initialDate,
        finalDate = finalDate,
        userEmail = userEmail
    )
}