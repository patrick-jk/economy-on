package com.economiaon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.economiaon.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user_entity WHERE email = :email")
    suspend fun getUser(email: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: UserEntity): Void

    @Query("DELETE FROM user_entity WHERE email = :email")
    suspend fun deleteUser(email: String): Void
}