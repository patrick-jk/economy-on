package com.economiaon.connection.repo

import com.economiaon.domain.Finance
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    suspend fun getFinancesByUserId(userId: Long) : Flow<List<Finance>>
}