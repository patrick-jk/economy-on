package com.economiaon.ui.navigation.finances

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.economiaon.R

class FinancesOverviewViewModel(context: Context) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = context.getString(R.string.title_finances)
    }
    val text: LiveData<String> = _text
}