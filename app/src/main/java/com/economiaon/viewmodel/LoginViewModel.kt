package com.economiaon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.repo.UserRepository
import com.economiaon.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isUserLogged = MutableLiveData<Boolean>()
    val isUserLogged: LiveData<Boolean> = _isUserLogged
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value = false
        }
    }

    fun checkUserInfo(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val findUserByEmail = userRepository.findUserByEmail(email)
            findUserByEmail.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val user = response.body()
                        _userInfo.postValue(user)
                        if (user?.password == password ) {
                            _isUserLogged.postValue(true)
                        }
                    } else {
                        _isUserLogged.postValue(false)
                        _userInfo.postValue(null)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _isUserLogged.postValue(false)
                    _userInfo.postValue(null)
                }
            })
        }
    }
}