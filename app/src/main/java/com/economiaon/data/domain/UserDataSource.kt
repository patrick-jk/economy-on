package com.economiaon.data.domain

import com.economiaon.domain.model.User

interface UserDataSource {
    suspend fun findUser(userId: String): User
    suspend fun saveUser(user: User): User
    suspend fun updateUser(user: User): Void

    suspend fun deleteUser(user: User): Void
}