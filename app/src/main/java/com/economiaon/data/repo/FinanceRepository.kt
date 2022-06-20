package com.economiaon.data.connection.repo

import com.economiaon.data.connection.service.ApiService
import com.economiaon.data.model.Finance
import kotlinx.coroutines.flow.flow

class FinanceRepository(private val apiService: ApiService) {
    suspend fun getFinancesByUserId(userId: Long) = flow<List<Finance>> {
        apiService.getFinancesByUserId(userId)
    }

    suspend fun saveFinance(finance: Finance) = apiService.saveFinance(finance)

    suspend fun updateFinance(finance: Finance) = apiService.updateFinance(finance)

    suspend fun deleteFinance(financeId: Long) = apiService.deleteFinance(financeId)
}