package com.economiaon.connection.repo

import com.economiaon.connection.service.ApiService
import com.economiaon.domain.User

class UserRepository(private val apiService: ApiService) {
    suspend fun saveUser(user: User) = apiService.saveUser(user)

    suspend fun updateUser(user: User) = apiService.updateUser(user)

    suspend fun findUserById(userId: Long) = apiService.findUserById(userId)

    suspend fun findUserByEmail(email: String) = apiService.findUserByEmail(email)
}