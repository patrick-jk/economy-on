package com.economiaon.ui.navigation.profile

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.economiaon.R

class ProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<Int>().apply {
        value = R.string.title_profile
    }
    @StringRes
    val text: LiveData<Int> = _text
}