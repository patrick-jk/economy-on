package com.economiaon.domain.usecase

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class DeleteFinanceUseCase(private val financeRepository: FinanceRepository): UseCase<Long, Void>() {
    override suspend fun execute(param: Long): Flow<Void> {
        return financeRepository.deleteFinance(param)
    }
}