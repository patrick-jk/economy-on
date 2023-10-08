package com.economiaon.data.domain

import com.economiaon.domain.model.Finance

interface FinanceDataSource {
    suspend fun getFinancesByUser(userEmail: String): List<Finance>
    suspend fun saveFinance(finance: Finance): Finance
    suspend fun updateFinance(finance: Finance): Void
    suspend fun deleteAllFinancesByUser(userEmail: String): Void
}