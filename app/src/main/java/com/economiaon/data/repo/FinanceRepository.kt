package com.economiaon.data.repo

import com.economiaon.data.local.impl.RoomFinanceDataSource
import com.economiaon.data.remote.impl.FirebaseFinanceDataSource
import com.economiaon.domain.model.Finance
import com.economiaon.util.throwRemoteException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class FinanceRepository(private val firebaseFinanceDataSource: FirebaseFinanceDataSource,
                        private val roomFinanceDataSource: RoomFinanceDataSource) {

    suspend fun getFinancesByUserId(userEmail: String) = flow {
        try {
            val financeList = firebaseFinanceDataSource.getFinancesByUser(userEmail)
            roomFinanceDataSource.deleteAllFinancesByUser(userEmail)
            roomFinanceDataSource.insertFinances(financeList)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible accessing data at moment.")
        }

        val updatedFinanceList = roomFinanceDataSource.getFinancesByUser(userEmail)
        emit(updatedFinanceList)
    }

    suspend fun saveFinance(finance: Finance) = flow {
        try {
            val savedFinance = firebaseFinanceDataSource.saveFinance(finance)
            emit(savedFinance)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible saving the finance.")
        }
    }

    suspend fun updateFinance(finance: Finance) = flow {
        try {
            val updatedFinance = firebaseFinanceDataSource.updateFinance(finance)
            emit(updatedFinance)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible saving the finance.")
        }
    }

    suspend fun deleteFinance(financeId: String) = flow {
        try {
            val deletedFinance = firebaseFinanceDataSource.deleteAllFinancesByUser(financeId)
            emit(deletedFinance)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible deleting the finance.")
        } catch (e: Exception) {
            throw e
        }
    }
}