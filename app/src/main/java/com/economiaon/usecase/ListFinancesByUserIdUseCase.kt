package com.economiaon.usecase

import com.economiaon.data.repo.FinanceRepository
import com.economiaon.data.model.Finance
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class ListFinancesByUserIdUseCase(private val financeRepository: FinanceRepository) :
    UseCase<Long, List<Finance>>() {
    override suspend fun execute(param: Long): Flow<List<Finance>> {
        return financeRepository.getFinancesByUserId(param)
    }
}