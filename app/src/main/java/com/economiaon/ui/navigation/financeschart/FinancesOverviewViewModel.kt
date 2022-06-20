package com.economiaon.ui.navigation.financeschart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FinancesOverviewViewModel : ViewModel() {

    private val _text = MutableLiveData<Int>().apply {
        value = 0
    }
    val text: LiveData<Int> = _text
}