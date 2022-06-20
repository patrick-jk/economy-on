package com.economiaon.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long, var name: String, var cpf: String,
    var email: String,
    @SerializedName("cellphone_number") var cellphoneNumber: String,
    var password: String,
    var age: Int, var salary: Float) : Parcelable
