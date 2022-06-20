package com.economiaon.ui.navigation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.model.User
import com.economiaon.data.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    fun getUserById(userId: Long) {
        viewModelScope.launch {
            val user = userRepository.findUserById(userId)
            user.enqueue(object: Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        _userInfo.postValue(response.body())
                    } else {
                        _userInfo.postValue(null)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _userInfo.postValue(null)
                }

            })
        }
    }
}