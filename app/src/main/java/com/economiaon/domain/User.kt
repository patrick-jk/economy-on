package com.economiaon.domain

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long, var name: String, var cpf: String,
    var email: String,
    @SerializedName("cellphone_number") var cellphoneNumber: String,
    var password: String,
    var age: Int, var salary: Float
)
