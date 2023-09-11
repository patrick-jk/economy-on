package com.economiaon.presentation.statepattern

import com.economiaon.domain.model.User

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val error: Throwable) : UserState()
}
