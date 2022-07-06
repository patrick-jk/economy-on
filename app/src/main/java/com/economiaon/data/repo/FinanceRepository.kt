package com.economiaon.data.repo

import com.economiaon.data.connection.service.ApiService
import com.economiaon.data.model.Finance
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FinanceRepository(private val apiService: ApiService) {
    suspend fun getFinancesByUserId(userId: Long) = flow {
        try {
            val financeList = apiService.getFinancesByUserId(userId)
            emit(financeList)
        } catch (e: HttpException) {
            e.printStackTrace()
        }

    }

    fun saveFinance(finance: Finance) = apiService.saveFinance(finance)

    fun updateFinance(finance: Finance) = apiService.updateFinance(finance)
}