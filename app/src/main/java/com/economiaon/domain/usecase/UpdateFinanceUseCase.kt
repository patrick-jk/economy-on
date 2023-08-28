package com.economiaon.domain.usecase

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.domain.model.Finance
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class UpdateFinanceUseCase(private val financeRepository: FinanceRepository) :
    UseCase<Finance, Void>() {
    override suspend fun execute(param: Finance): Flow<Void> {
        return financeRepository.updateFinance(param)
    }
}