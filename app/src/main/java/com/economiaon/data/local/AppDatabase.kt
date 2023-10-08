package com.economiaon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.economiaon.data.local.dao.FinanceDao
import com.economiaon.data.local.dao.UserDao
import com.economiaon.data.local.entity.FinanceEntity
import com.economiaon.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, FinanceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun financeDao(): FinanceDao
}