package com.economiaon.presentation.edituser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.domain.usecase.UpdateUserUseCase
import com.economiaon.domain.model.User
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditUserViewModel(private val updateUserUseCase: UpdateUserUseCase): ViewModel() {
    private val _updateUser = MutableLiveData<Boolean>()
    val updateUser: LiveData<Boolean> = _updateUser

    fun updateUser(user: User) = viewModelScope.launch {
        updateUserUseCase(user)
            .catch {
                _updateUser.postValue(false)
            }
            .collect {
                _updateUser.postValue(true)
            }
    }
}