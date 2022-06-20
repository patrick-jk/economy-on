package com.economiaon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.repo.UserRepository
import com.economiaon.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isUserRegistered = MutableLiveData<Boolean>()
    val isUserRegistered: LiveData<Boolean> = _isUserRegistered

    fun checkUserInfo(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val findUserByEmail = userRepository.findUserByEmail(email)
            findUserByEmail.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val user = response.body()
                        if (user?.password == password ) {
                            _isUserRegistered.postValue(true)
                        }
                    } else {
                        _isUserRegistered.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _isUserRegistered.postValue(false)
                }
            })
        }
    }
}