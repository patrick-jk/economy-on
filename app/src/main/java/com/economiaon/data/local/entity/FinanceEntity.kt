package com.economiaon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.economiaon.domain.model.Finance
import com.economiaon.domain.model.FinanceType

@Entity(tableName = "finance_entity")
data class FinanceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: FinanceType,
    @ColumnInfo(name = "finance_cost") val financeCost: Double,
    @ColumnInfo(name = "initial_date") val initialDate: String,
    @ColumnInfo(name = "final_date") val finalDate: String,
    @ColumnInfo(name = "user_email") val userEmail: String
) {
    fun toFinance() = Finance(
        id = id,
        name = name,
        type = type,
        financeCost = financeCost,
        initialDate = initialDate,
        finalDate = finalDate,
        userEmail = userEmail
    )
}
