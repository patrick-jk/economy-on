package com.economiaon.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.usecase.FindUserByEmailUseCase
import com.economiaon.domain.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val findUserByEmailUseCase: FindUserByEmailUseCase) : ViewModel() {
    private val _isUserLogged = MutableLiveData<Boolean>()
    val isUserLogged: LiveData<Boolean> = _isUserLogged

    private val _loggedUser = MutableLiveData<User>()
    val loggedUser: LiveData<User> = _loggedUser

    fun checkUserInfo(email: String, password: String) = viewModelScope.launch {
        findUserByEmailUseCase(email)
            .catch {
                _isUserLogged.postValue(false)
            }
            .collect {
                if (it.password == password) {
                    _isUserLogged.postValue(true)
                    _loggedUser.postValue(it)
                } else {
                    _isUserLogged.postValue(false)
                }
            }
    }
}
