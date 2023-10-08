package com.economiaon.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.economiaon.domain.model.User

@Entity(tableName = "user_entity")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val cpf: String,
    val email: String,
    val password: String,
    @ColumnInfo(name = "cellphone_number") var cellphoneNumber: String,
    var age: Int,
    var salary: Float
) {
    fun toUser() = User(
        id = id,
        name = name,
        cpf = cpf,
        email = email,
        password = password,
        cellphoneNumber = cellphoneNumber,
        age = age,
        salary = salary
    )
}
