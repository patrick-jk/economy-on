package com.economiaon.ui.navigation.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.economiaon.R

class ProfileViewModel(context: Context) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = context.getString(R.string.title_profile)
    }
    val text: LiveData<String> = _text
}