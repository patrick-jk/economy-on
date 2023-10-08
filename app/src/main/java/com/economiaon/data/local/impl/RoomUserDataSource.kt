package com.economiaon.data.local.impl

import com.economiaon.data.domain.UserDataSource
import com.economiaon.data.local.dao.UserDao
import com.economiaon.domain.model.User

class RoomUserDataSource(private val userDao: UserDao) : UserDataSource {
    override suspend fun findUser(userId: String): User {
        return userDao.getUser(userId).toUser()
    }

    override suspend fun saveUser(user: User): User {
        userDao.insertUser(user.toUserEntity())
        return user
    }

    override suspend fun updateUser(user: User): Void {
        return userDao.updateUser(user.toUserEntity())
    }

    override suspend fun deleteUser(user: User): Void {
        return userDao.deleteUser(user.id)
    }
}