package com.economiaon.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.usecase.FindUserByEmailUseCase
import com.economiaon.domain.usecase.SaveUserUseCase
import com.economiaon.domain.model.User
import com.economiaon.presentation.statepattern.UserState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(
    private val saveUserUseCase: SaveUserUseCase,
    private val findUserByEmailUseCase: FindUserByEmailUseCase
) : ViewModel() {

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    private val _registerUser = MutableLiveData<UserState>()
    val registerUser: LiveData<UserState> = _registerUser

    private val _isUserAlreadyRegistered = MutableLiveData<Boolean>()
    val isUserAlreadyRegistered: LiveData<Boolean> = _isUserAlreadyRegistered


    fun saveUser(user: User) = viewModelScope.launch {
        saveUserUseCase(user)
            .onStart {
                _registerUser.postValue(UserState.Loading)
            }
            .catch {
                _registerUser.postValue(UserState.Error(it))
            }
            .collect {
                it.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            _registerUser.postValue(UserState.Success(response.body()!!))
                            _status.postValue(true)
                        } else {
                            _registerUser.postValue(UserState.Error(Throwable(response.message())))
                            _status.postValue(false)
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        _registerUser.postValue(UserState.Error(t))
                    }

                })
            }
    }

    fun findUserByEmail(userEmail: String) = viewModelScope.launch {
        findUserByEmailUseCase(userEmail)
            .onStart {
            }
            .catch {
                _isUserAlreadyRegistered.postValue(false)
            }
            .collect {
                it.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful)
                            _isUserAlreadyRegistered.value = userEmail == response.body()?.email
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        _isUserAlreadyRegistered.postValue(false)
                    }

                })
            }
    }
}