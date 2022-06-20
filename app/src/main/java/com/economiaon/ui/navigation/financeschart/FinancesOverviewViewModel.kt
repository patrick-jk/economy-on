package com.economiaon.ui.navigation.finances

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.economiaon.R

class FinancesOverviewViewModel : ViewModel() {

    private val _text = MutableLiveData<Int>().apply {
        value = 0
    }
    val text: LiveData<Int> = _text
}