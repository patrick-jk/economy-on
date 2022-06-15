package com.economiaon.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.connection.repo.UserRepository
import com.economiaon.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class RegisterActivityViewModel(private val repository: UserRepository) : ViewModel() {
    val status = MutableLiveData<Boolean>()
    val registeredUser by lazy { MutableLiveData<User>() }

    fun registerUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val postUser = repository.saveUser(user)
            postUser.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        status.postValue(true)
                        registeredUser.postValue(response.body())
                    } else {
                        status.postValue(false)
                        registeredUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    status.postValue(false)
                }
            })
        }

    }
}