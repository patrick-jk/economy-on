package com.economiaon.domain.usecase

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteFinanceUseCase(private val financeRepository: FinanceRepository): UseCase<String, Void>() {
    override suspend fun execute(param: String): Flow<Void> {
        return financeRepository.deleteFinance(param)
    }
}