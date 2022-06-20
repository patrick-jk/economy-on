package com.economiaon.data.repo

import com.economiaon.data.connection.service.ApiService
import com.economiaon.data.model.User

class UserRepository(private val apiService: ApiService) {
    fun saveUser(user: User) = apiService.saveUser(user)

    fun updateUser(user: User) = apiService.updateUser(user)

    fun findUserById(userId: Long) = apiService.findUserById(userId)

    fun findUserByEmail(email: String) = apiService.findUserByEmail(email)
}