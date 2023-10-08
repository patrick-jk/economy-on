package com.economiaon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.economiaon.data.local.entity.FinanceEntity

@Dao
interface FinanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFinances(finance: List<FinanceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Query("SELECT * FROM finance_entity WHERE user_email = :userEmail")
    suspend fun getFinancesByUser(userEmail: String): List<FinanceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFinance(finance: FinanceEntity): Void

    @Query("DELETE FROM finance_entity WHERE user_email = :userEmail")
    suspend fun deleteAllFinances(userEmail: String): Void
}