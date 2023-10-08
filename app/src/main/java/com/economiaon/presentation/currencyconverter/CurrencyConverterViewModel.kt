package com.economiaon.presentation.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.model.CurrencyApiResponse
import com.economiaon.data.model.CurrencyConversionInfo
import com.economiaon.domain.usecase.ConvertCurrencyUseCase
import com.economiaon.presentation.statepattern.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyConverterViewModel(private val convertCurrencyUseCase: ConvertCurrencyUseCase) : ViewModel() {
    private val _currencyConverted = MutableLiveData<State<CurrencyApiResponse>>()
    val currencyConverted: LiveData<State<CurrencyApiResponse>> = _currencyConverted

    fun convertCurrency(currencyConversionInfo: CurrencyConversionInfo) {
        viewModelScope.launch {
            convertCurrencyUseCase(currencyConversionInfo)
                .onStart {
                    _currencyConverted.value = State.Loading("Converting currency...")
                }
                .catch {
                    _currencyConverted.value = State.Error(it)
                }
                .collect {
                    it.enqueue(object : Callback<CurrencyApiResponse> {
                        override fun onResponse(call: Call<CurrencyApiResponse>, response: Response<CurrencyApiResponse>) {
                            if (response.isSuccessful) {
                                _currencyConverted.value = State.Success(response.body()!!)
                                return
                            } else {
                                _currencyConverted.value = State.Error(Throwable("It was not possible to convert currency."))
                            }
                        }

                        override fun onFailure(call: Call<CurrencyApiResponse>, t: Throwable) {
                            _currencyConverted.value = State.Error(t)
                        }

                    })
                }
        }
    }
}