package com.economiaon.presentation.taxcalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TaxCalculatorViewModel : ViewModel() {
    private val _valueAfterTaxes = MutableSharedFlow<Double>()
    val valueAfterTaxes: SharedFlow<Double> = _valueAfterTaxes

    fun calculateValueAfterTaxes(value: Double, tax: Double) {
        viewModelScope.launch {
            _valueAfterTaxes.emit(value - (value * tax / 100))
        }
    }
}