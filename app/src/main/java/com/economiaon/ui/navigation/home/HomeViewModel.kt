package com.economiaon.ui.navigation.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.economiaon.R

class HomeViewModel(context: Context) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = context.getString(R.string.txt_used_balance)
    }
    val text: LiveData<String> = _text
}