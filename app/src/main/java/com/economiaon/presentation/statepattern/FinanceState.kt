package com.economiaon.presentation.statepattern

import com.economiaon.domain.model.Finance

sealed class FinanceState {
    object Loading : FinanceState()
    data class Success(val list: List<Finance>) : FinanceState()
    data class Error(val error: Throwable) : FinanceState()
}