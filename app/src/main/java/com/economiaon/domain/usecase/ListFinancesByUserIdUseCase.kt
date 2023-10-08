package com.economiaon.domain.usecase

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.domain.model.Finance
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class ListFinancesByUserIdUseCase(private val financeRepository: FinanceRepository) :
    UseCase<String, List<Finance>>() {
    override suspend fun execute(param: String): Flow<List<Finance>> {
        return financeRepository.getFinancesByUserId(param)
    }
}