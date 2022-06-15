package com.economiaon.connection.repo

import com.economiaon.connection.service.ApiService
import com.economiaon.domain.Finance
import retrofit2.http.Body

class FinanceRepository(private val apiService: ApiService) {
    suspend fun getFinancesByUserId(userId: Long) = apiService.getFinancesByUserId(userId)

    suspend fun saveFinance(finance: Finance) = apiService.saveFinance(finance)

    suspend fun updateFinance(finance: Finance) = apiService.updateFinance(finance)

    suspend fun deleteFinance(financeId: Long) = apiService.deleteFinance(financeId)
}