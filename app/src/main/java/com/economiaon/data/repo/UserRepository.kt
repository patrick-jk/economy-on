package com.economiaon.data.repo

import com.economiaon.data.local.impl.RoomUserDataSource
import com.economiaon.data.remote.impl.FirebaseUserDataSource
import com.economiaon.domain.model.User
import com.economiaon.util.throwRemoteException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepository(
    private val firebaseUserDataSource: FirebaseUserDataSource,
    private val roomUserDataSource: RoomUserDataSource
) {

    suspend fun saveUser(user: User) = flow {
        try {
            val savedUser = firebaseUserDataSource.saveUser(user)
            emit(savedUser)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible saving the user.")
        }
    }

    suspend fun updateUser(user: User) = flow {
        try {
            val newUser = firebaseUserDataSource.updateUser(user)
            emit(newUser)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible updating the user.")
        }
    }

    suspend fun findUserById(userId: String) = flow {
        try {
            val user = firebaseUserDataSource.findUser(userId)
            roomUserDataSource.deleteUser(user)
            roomUserDataSource.saveUser(user)
            emit(user)
        } catch (e: HttpException) {
            e.throwRemoteException("It was not possible find the user by id.")
        }

        val updatedUser = roomUserDataSource.findUser(userId)
        emit(updatedUser)
    }
}