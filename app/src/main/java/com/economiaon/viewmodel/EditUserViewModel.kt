package com.economiaon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.economiaon.data.model.User
import com.economiaon.data.repo.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class EditUserViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _updateUser = MutableLiveData<Boolean>()
    val updateUser: LiveData<Boolean> = _updateUser

    fun updateUser(user: User) {
        val newUser = userRepository.updateUser(user)
        newUser.enqueue(object : Callback<Nothing> {
            override fun onResponse(call: Call<Nothing>, response: Response<Nothing>) {
                if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                    _updateUser.postValue(true)
                } else {
                    _updateUser.postValue(false)
                }
            }

            override fun onFailure(call: Call<Nothing>, t: Throwable) {
                _updateUser.postValue(false)
            }

        })
    }
}