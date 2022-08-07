package com.economiaon.data.repo

import com.economiaon.data.ApiService
import com.economiaon.domain.model.Finance
import com.economiaon.util.throwRemoteException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FinanceRepository(private val apiService: ApiService) {

    suspend fun getFinancesByUserId(userId: Long) = flow {
        try {
            val financeList = apiService.getFinancesByUserId(userId)
            emit(financeList)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible accessing data at moment.")
        }

    }

    suspend fun saveFinance(finance: Finance) = flow {
        try {
            val savedFinance = apiService.saveFinance(finance)
            emit(savedFinance)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible saving the finance.")
        }
    }

    suspend fun updateFinance(finance: Finance) = flow {
        try {
            val updatedFinance = apiService.updateFinance(finance)
            emit(updatedFinance)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible saving the finance.")
        }
    }

    suspend fun deleteFinance(financeId: Long) = flow {
        try {
            val deletedFinance = apiService.deleteFinance(financeId)
            emit(deletedFinance)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible deleting the finance.")
        }
    }
}