package com.economiaon.domain.model

import android.os.Parcelable
import com.economiaon.data.local.entity.UserEntity
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String,
    var cpf: String,
    var email: String,
    @get:PropertyName("cellphone_number") @set:PropertyName("cellphone_number") var cellphoneNumber: String,
    var password: String,
    var age: Int,
    var salary: Float
) : Parcelable {
    constructor() : this("", "", "", "", "", "", 0, 0.0f)

    fun toUserEntity() = UserEntity(
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
