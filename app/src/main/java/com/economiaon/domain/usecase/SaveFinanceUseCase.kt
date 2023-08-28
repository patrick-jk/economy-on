package com.economiaon.domain.usecase

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.domain.model.Finance
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class SaveFinanceUseCase(private val financeRepository: FinanceRepository) :
    UseCase<Finance, Finance>() {
    override suspend fun execute(param: Finance): Flow<Finance> {
        return financeRepository.saveFinance(param)
    }
}