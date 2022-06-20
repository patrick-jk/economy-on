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

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status
    private val _registerUser = MutableLiveData<User>()
    val registerUser: LiveData<User> = _registerUser
    private val _isUserAlreadyRegistered = MutableLiveData<Boolean>()
    val isUserAlreadyRegistered: LiveData<Boolean> = _isUserAlreadyRegistered


    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val postUser = repository.saveUser(user)
            postUser.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        _status.postValue(true)
                        _registerUser.postValue(response.body())
                    } else {
                        _status.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _status.postValue(false)
                }
            })
        }

    }

    fun findUserByEmail(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userByEmail = repository.findUserByEmail(userEmail)
            userByEmail.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        _isUserAlreadyRegistered.postValue(true)
                    } else {
                        _isUserAlreadyRegistered.postValue(false)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _isUserAlreadyRegistered.postValue(false)
                }

            })
        }
    }
}