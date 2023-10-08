package com.economiaon.data.local.impl

import com.economiaon.data.domain.FinanceDataSource
import com.economiaon.data.local.dao.FinanceDao
import com.economiaon.domain.model.Finance

class RoomFinanceDataSource(
    private val financeDao: FinanceDao
) : FinanceDataSource {
    override suspend fun getFinancesByUser(userEmail: String): List<Finance> {
        return financeDao.getFinancesByUser(userEmail).map { it.toFinance() }
    }

    override suspend fun saveFinance(finance: Finance): Finance {
        financeDao.insertFinance(finance.toFinanceEntity())
        return finance
    }

    suspend fun insertFinances(financeList: List<Finance>) {
        financeDao.insertAllFinances(financeList.map { it.toFinanceEntity() })
    }

    override suspend fun updateFinance(finance: Finance): Void {
        return financeDao.updateFinance(finance.toFinanceEntity())
    }

    override suspend fun deleteAllFinancesByUser(userEmail: String): Void {
        return financeDao.deleteAllFinances(userEmail)
    }
}