package com.economiaon.connection.repo.impl

import android.os.RemoteException
import com.economiaon.connection.repo.FinanceRepository
import com.economiaon.connection.service.ApiService
import com.economiaon.domain.Finance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FinanceRepositoryImpl(private val apiService: ApiService) : FinanceRepository {
    override suspend fun getFinancesByUserId(userId: Long): Flow<List<Finance>> = flow {
        try {
            val financeList = apiService.getFinancesByUserId(userId)
            emit(financeList)
        } catch (e: HttpException) {
            throw RemoteException(e.message() ?: "Impossible to find the finances at the moment.")
        }
    }
}