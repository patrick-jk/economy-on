package com.economiaon.data.repo

import com.economiaon.data.model.CurrencyConversionInfo
import com.economiaon.data.remote.ApiService
import com.economiaon.util.throwRemoteException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class CurrencyRepository(private val api: ApiService) {
    suspend fun getCurrencyConversion(currencyConversionInfo: CurrencyConversionInfo) = flow {
        try {
            val currencyConversion = api.convertCurrency(fromCurrency = currencyConversionInfo.from,
                toCurrency = currencyConversionInfo.to, value = currencyConversionInfo.value)

            emit(currencyConversion)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible accessing data at moment.")
        }
    }

}

