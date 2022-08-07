package com.economiaon.data.repo

import com.economiaon.data.ApiService
import com.economiaon.domain.model.User
import com.economiaon.util.throwRemoteException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository(private val apiService: ApiService) {

    suspend fun saveUser(user: User) = flow {
        try {
            val savedUser = apiService.saveUser(user)
            emit(savedUser)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible saving the user.")
        }
    }

    suspend fun updateUser(user: User) = flow {
        try {
            val updatedUser = apiService.updateUser(user)
            emit(updatedUser)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible updating the user.")
        }
    }

    suspend fun findUserById(userId: Long) = flow {
        try {
            val user = apiService.findUserById(userId)
            emit(user)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible find the user by id.")
        }
    }

    suspend fun findUserByEmail(email: String) = flow {
        try {
            val user = apiService.findUserByEmail(email)
            emit(user)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible find the user by email.")
        }
    }
}