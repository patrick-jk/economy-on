package com.economiaon.domain.usecase

import android.util.Log
import com.economiaon.data.model.CurrencyApiResponse
import com.economiaon.data.model.CurrencyConversionInfo
import com.economiaon.data.repo.CurrencyRepository
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class ConvertCurrencyUseCase(private val currencyRepository: CurrencyRepository) : UseCase<CurrencyConversionInfo, Call<CurrencyApiResponse>>() {
    override suspend fun execute(param: CurrencyConversionInfo): Flow<Call<CurrencyApiResponse>> {
        Log.d("ConvertCurrencyUseCase", "execute: $param")
        return currencyRepository.getCurrencyConversion(param)
    }
}